package pl.tscript3r.photogram2.services;

import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

public interface PostService {

    List<PostDto> getLatest(@Nullable final Integer count);

    List<PostDto> getLatest(@NotNull final String username, @Nullable final Integer count);

    List<PostDto> getLatestUsersPosts(@Nullable final Principal principal, @Nullable final Integer count);

    PostDto save(@Nullable final Principal principal, @NotNull final PostDto postDto);

    PostDto update(@Nullable final Principal principal, @NotNull final PostDto postDto);

    void delete(@Nullable final Principal principal, @NotNull final Long postId);

    PostDto like(@Nullable final Principal principal, @NotNull final Long postId);

    PostDto unlike(@Nullable final Principal principal, @NotNull final Long postId);

    PostDto dislike(@Nullable final Principal principal, @NotNull final Long postId);

    PostDto undislike(@Nullable final Principal principal, @NotNull final Long postId);

}
