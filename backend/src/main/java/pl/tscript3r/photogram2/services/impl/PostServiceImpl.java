package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.IgnoredPhotogramException;
import pl.tscript3r.photogram2.exceptions.InternalErrorPhotogramException;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.PostRepository;
import pl.tscript3r.photogram2.services.ImageService;
import pl.tscript3r.photogram2.services.PostService;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final RoleService roleService;
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final MapperService mapperService;

    @Override
    public Slice<PostDto> getLatest(@NotNull final Pageable pageable) {
        return getDtoSliceOf(postRepository.findAllByValidIsTrue(pageable).getContent());
    }

    private Slice<PostDto> getDtoSliceOf(final List<Post> posts) {
        return new SliceImpl<>(mapperService.map(posts, PostDto.class));
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final String username, @NotNull final Pageable pageable) {
        User user = userService.getByUsername(username);
        return getLatestDtosFromUser(user, pageable);
    }

    private Slice<PostDto> getLatestDtosFromUser(final User user, final Pageable pageable) {
        return getDtoSliceOf(postRepository.findByUserIdAndValidIsTrue(user.getId(), pageable).getContent());
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final Principal principal, @NotNull final Pageable pageable) {
        User user = userService.getByPrincipal(principal);
        return getLatestDtosFromUser(user, pageable);
    }

    @Override
    public Post getById(@NotNull final Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Post id=%s not found", id)));
    }

    @Override
    public PostDto getByIdDto(@NotNull final Long id) {
        return mapperService.map(getById(id), PostDto.class);
    }

    @Override
    public PostDto save(final Principal principal, @NotNull final PostDto postDto) {
        roleService.requireLogin(principal)
                .accessValidation(principal, postDto.getUserId());
        var post = mapperService.map(postDto, Post.class);
        if (post.getId() != null)
            post.setId(null);
        checkUserId(principal, post);
        post.setImageId(imageService.reserveNextImageId(post.getUser().getId()));
        return mapperService.map(postRepository.save(post), PostDto.class);
    }

    private void checkUserId(final Principal principal, final Post post) {
        if (post.getUser() == null)
            post.setUser(userService.getByPrincipal(principal));
    }

    @Override
    public PostDto update(final Principal principal, @NotNull final Long id, @NotNull final PostDto postDto) {
        var updatedPost = mapperService.map(postDto, Post.class);
        var originalPost = getById(id);
        roleService.requireLogin(principal)
                .accessValidation(principal, originalPost.getUser().getId());
        updateValues(originalPost, updatedPost);
        postRepository.save(originalPost);
        return mapperService.map(originalPost, PostDto.class);
    }

    private void updateValues(Post originalPost, Post updatedPost) {
        if (updatedPost.getCaption() != null)
            originalPost.setCaption(updatedPost.getCaption());
    }

    @Override
    public void delete(final Principal principal, @NotNull final Long id) {
        var post = getById(id);
        roleService.requireLogin(principal)
                .accessValidation(principal, post.getUser().getId());
        postRepository.delete(post);
    }

    @Override
    public PostDto react(@NotNull final Reactions reaction, final Principal principal, @NotNull final Long id) {
        roleService.requireLogin(principal);
        var post = getById(id);
        var reactedByUser = userService.getByPrincipal(principal);
        switch (reaction) {
            case LIKE:
                if (reactedByUser.addLikedPost(post))
                    post.incrementLikes();
                else
                    throwAlreadyReactedIgnoredException();
                break;
            case UNLIKE:
                if (reactedByUser.removeLikedPost(post))
                    post.decrementLikes();
                else
                    throwNotReactedIgnoredException();
                break;
            case DISLIKE:
                if (reactedByUser.addDislikedPost(post))
                    post.incrementDislikes();
                else
                    throwAlreadyReactedIgnoredException();
                break;
            case UNDISLIKE:
                if (reactedByUser.removeDislikedPost(post))
                    post.decrementDislikes();
                else
                    throwNotReactedIgnoredException();
                break;
            default:
                throw new InternalErrorPhotogramException("Not recognized reaction=" + reaction.name());
        }
        checkIfAlreadyReactedOppositeAndIfRemove(reaction, reactedByUser, post);
        post = postRepository.save(post);
        userService.save(reactedByUser, false, false);
        return mapperService.map(post, PostDto.class);
    }

    private void checkIfAlreadyReactedOppositeAndIfRemove(final Reactions reaction, final User user, final Post post) {
        if (reaction == Reactions.LIKE && user.hasDislikedPost(post))
            user.removeDislikedPost(post);
        if (reaction == Reactions.DISLIKE && user.hasLikedPost(post))
            user.removeLikedPost(post);
    }

    private void throwAlreadyReactedIgnoredException() {
        throw new IgnoredPhotogramException("Already reacted that way, ignored");
    }

    private void throwNotReactedIgnoredException() {
        throw new IgnoredPhotogramException("Not reacted that way, ignored");
    }

}
