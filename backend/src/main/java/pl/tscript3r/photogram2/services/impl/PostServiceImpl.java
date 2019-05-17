package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.repositories.PostRepository;
import pl.tscript3r.photogram2.services.PostService;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final MapperService mapperService;

    @Override
    public Slice<PostDto> getLatest(@NotNull final Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final String username, @NotNull final Pageable pageable) {
        return null;
    }

    @Override
    public Slice<PostDto> getLatest(@NotNull final Principal principal, @NotNull final Pageable pageable) {
        return null;
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
