package pl.tscript3r.photogram2.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.domains.Post;

import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface PostService {

    enum Reactions {LIKE, UNLIKE, DISLIKE, UNDISLIKE}

    Slice<PostDto> getLatest(@NotNull final Pageable pageable);

    Slice<PostDto> getLatest(@NotNull final String username, @NotNull final Pageable pageable);

    Slice<PostDto> getLatest(@NotNull final Principal principal, @NotNull final Pageable pageable);

    Post getById(@NotNull final Long id);

    PostDto getByIdDto(@NotNull final Long id);

    PostDto save(@Nullable final Principal principal, @NotNull final PostDto postDto);

    PostDto update(@Nullable final Principal principal, @NotNull final Long id, @NotNull final PostDto postDto);

    void delete(@Nullable final Principal principal, @NotNull final Long id);

    PostDto react(@NotNull Reactions reaction, @Nullable final Principal principal, @NotNull final Long id);

}
