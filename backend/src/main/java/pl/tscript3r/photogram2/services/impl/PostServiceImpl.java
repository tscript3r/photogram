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
        roleService.accessValidation(principal, originalPost.getUser().getId());
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
    public PostDto react(@NotNull Reactions reaction, Principal principal, @NotNull Long id) {
        roleService.requireLogin(principal);
        var post = getById(id);
        var reactedBy = userService.getByPrincipal(principal);
        switch (reaction) {
            case LIKE:
                if (reactedBy.addLikedPost(post))
                    post.incrementLikes();
                break;
            case UNLIKE:
                if (reactedBy.removeLikedPost(post))
                    post.decrementLikes();
                break;
            case DISLIKE:
                if (reactedBy.addDislikedPost(post))
                    post.incrementDislikes();
                break;
            case UNDISLIKE:
                if (reactedBy.removeDislikedPost(post))
                    post.decrementDislikes();
                break;
            default:
                throw new InternalErrorPhotogramException("Not recognized reaction");
        }
        post = postRepository.save(post);
        userService.save(reactedBy, false, false);
        return mapperService.map(post, PostDto.class);
    }

}
