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
import pl.tscript3r.photogram2.repositories.PostRepository;
import pl.tscript3r.photogram2.services.PostService;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserService userService;
    private final PostRepository postRepository;
    private final MapperService mapperService;

    @Override
    public Slice<PostDto> getLatest(@NotNull final Pageable pageable) {
        return getDtoSliceOf(postRepository.findAll(pageable).getContent());
    }

    private Slice<PostDto> getDtoSliceOf(List<Post> posts) {
        return new SliceImpl<>(mapperService.map(posts, PostDto.class));
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final String username, @NotNull final Pageable pageable) {
        User user = userService.getByUsername(username);
        return getLatestDtosFromUser(user, pageable);
    }

    private Slice<PostDto> getLatestDtosFromUser(User user, Pageable pageable) {
        return getDtoSliceOf(postRepository.findByUserId(user.getId(), pageable).getContent());
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final Principal principal, @NotNull final Pageable pageable) {
        User user = userService.getByPrincipal(principal);
        return getLatestDtosFromUser(user, pageable);
    }

    @Override
    public PostDto save(final Principal principal, @NotNull final PostDto postDto) {
        return null;
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
