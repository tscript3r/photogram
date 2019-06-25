package pl.tscript3r.photogram.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import pl.tscript3r.photogram.api.v1.dtos.CommentDto;

import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface CommentService {

    Slice<CommentDto> getLatest(@NotNull final Long postId, @NotNull final Pageable pageable);

    CommentDto save(@Nullable final Principal principal, @NotNull final Long postId, @NotNull final CommentDto commentDto);

    CommentDto update(@Nullable final Principal principal, @NotNull final Long commentId,
                      @NotNull final CommentDto commentDto);


}
