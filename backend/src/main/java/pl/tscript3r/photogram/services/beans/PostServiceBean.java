package pl.tscript3r.photogram.services.beans;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.api.v1.dtos.PostDto;
import pl.tscript3r.photogram.api.v1.services.MapperService;
import pl.tscript3r.photogram.domains.Image;
import pl.tscript3r.photogram.domains.Post;
import pl.tscript3r.photogram.domains.User;
import pl.tscript3r.photogram.exceptions.IgnoredPhotogramException;
import pl.tscript3r.photogram.exceptions.InternalErrorPhotogramException;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.repositories.PostRepository;
import pl.tscript3r.photogram.services.AuthorizationService;
import pl.tscript3r.photogram.services.ImageService;
import pl.tscript3r.photogram.services.PostService;
import pl.tscript3r.photogram.services.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceBean implements PostService {

    private final UserService userService;
    private final AuthorizationService authorizationService;
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final MapperService mapperService;

    // TODO: refactor to use visibility scopes

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
        authorizationService.requireLogin(principal)
                .accessValidation(principal, postDto.getUserId());
        var post = mapperService.map(postDto, Post.class);
        if (post.getId() != null)
            post.setId(null);
        checkUserId(principal, post);
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
        authorizationService.requireLogin(principal)
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
        authorizationService.requireLogin(principal)
                .accessValidation(principal, post.getUser().getId());
        postRepository.delete(post);
    }

    @Override
    public PostDto react(@NotNull final Reactions reaction, final Principal principal, @NotNull final Long id) {
        authorizationService.requireLogin(principal);
        var post = getById(id);
        var reactedByUser = userService.getByPrincipal(principal);
        doReact(post, reactedByUser, reaction);
        checkIfAlreadyReactedOppositeAndIfRemove(reaction, reactedByUser, post);
        post = postRepository.save(post);
        userService.update(reactedByUser);
        return mapperService.map(post, PostDto.class);
    }

    private void doReact(final Post post, final User reactedByUser, final Reactions reaction) {
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

    @Override
    public PostDto saveImage(final Principal principal, @NotNull final Long id, @NotNull final MultipartFile imageFile) {
        var post = getById(id);
        authorizationService.requireLogin(principal)
                .accessValidation(principal, post.getUser().getId());
        var image = new Image(imageService.getNextId(post.getId()), getImageExtension(imageFile));
        post.addImage(image);
        imageService.savePostImage(id, image, imageFile);
        return mapperService.map(postRepository.save(post), PostDto.class);
    }

    private String getImageExtension(final MultipartFile multipartFile) {
        return FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    }

    @Override
    public ResponseEntity<byte[]> getImage(@NotNull final Long id, @NotNull final Long imageId) {
        var post = getById(id);
        return imageService.getPostImage(id, post.getImage(imageId));
    }

}
