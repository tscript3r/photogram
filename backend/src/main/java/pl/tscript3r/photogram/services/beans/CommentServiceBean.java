package pl.tscript3r.photogram.services.beans;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram.api.v1.services.MapperService;
import pl.tscript3r.photogram.repositories.CommentRepository;
import pl.tscript3r.photogram.services.AuthorizationService;
import pl.tscript3r.photogram.services.CommentService;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CommentServiceBean implements CommentService {

    private final CommentRepository commentRepository;
    private final AuthorizationService authorizationService;
    private final MapperService mapperService;

    @Override
    public Slice<CommentDto> getLatest(@NotNull final Long postId, @NotNull final Pageable pageable) {
        return null;
    }

    @Override
    public CommentDto save(Principal principal, @NotNull final Long postId, @NotNull final CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto update(Principal principal, @NotNull final Long commentId, @NotNull final CommentDto commentDto) {
        return null;
    }

    @Override
    public CommentDto add(Principal principal, @NotNull final Long postId, @NotNull final CommentDto commentDto) {
        return null;
    }

    @Override
    public void delete(Principal principal, @NotNull final Long id) {

    }

}
