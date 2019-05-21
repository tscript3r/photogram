package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.ForbiddenPhotogramException;
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
    public PostDto save(final Principal principal, @NotNull final PostDto postDto) {
        Post post = getPostAndValidateUsersAccessPermission(principal, postDto);
        post.setImageId(imageService.reserveNextImageId(post.getUser().getId()));
        return mapperService.map(postRepository.save(post), PostDto.class);
    }

    private Post getPostAndValidateUsersAccessPermission(final Principal principal, final PostDto postDto) {
        User requestedUser = userService.getByPrincipal(principal);
        if (postDto.getUserId() == null)
            postDto.setUserId(requestedUser.getId());
        Post post = mapperService.map(postDto, Post.class);
        if (!requestedUser.equals(post.getUser()) && !roleService.isAdmin(requestedUser))
            throw new ForbiddenPhotogramException("User needs to have admin rights to create posts for other users");
        return post;
    }

    @Override
    public PostDto update(final Principal principal, @NotNull final PostDto postDto) {
        return null;
    }

    @Override
    public void delete(final Principal principal, @NotNull final Long postId) {

    }

    @Override
    public PostDto like(final Principal principal, @NotNull final Long postId) {
        return null;
    }

    @Override
    public PostDto unlike(final Principal principal, @NotNull final Long postId) {
        return null;
    }

    @Override
    public PostDto dislike(final Principal principal, @NotNull final Long postId) {
        return null;
    }

    @Override
    public PostDto undislike(final Principal principal, @NotNull final Long postId) {
        return null;
    }

}
