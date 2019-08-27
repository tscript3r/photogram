package pl.tscript3r.photogram.post.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.infrastructure.mapper.MapperService;
import pl.tscript3r.photogram.post.PostService;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDto;
import pl.tscript3r.photogram.user.AuthorizationService;
import pl.tscript3r.photogram.user.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final PostService postService;
    private final MapperService mapperService;
    private final CommentRepository commentRepository;
    private final AuthorizationService authorizationService;

    public Slice<CommentDto> getLatest(@NotNull final Long postId, @NotNull final Pageable pageable) {
        var comments = commentRepository.findByPostId(postId, pageable);
        return new SliceImpl<>(mapperService.map(comments.getContent(), CommentDto.class));
    }

    public CommentDto save(final Principal principal, @NotNull final Long postId, @NotNull final CommentDto commentDto) {
        var userOptional = authorizationService.requireLogin(principal)
                .accessValidation(principal, commentDto.getUserId());
        var comment = mapperService.map(commentDto, Comment.class);
        comment.setUser(userOptional.orElse(userService.getByPrincipal(principal)));
        comment.setPost(postService.getById(postId));
        return mapperService.map(commentRepository.save(comment), CommentDto.class);
    }

    public CommentDto update(final Principal principal, @NotNull final Long id, @NotNull final CommentDto commentDto) {
        var comment = getById(id);
        authorizationService.requireLogin(principal)
                .accessValidation(principal, comment.getUser().getId());
        comment.setContent(commentDto.getContent());
        return mapperService.map(commentRepository.save(comment), CommentDto.class);
    }

    private Comment getById(@NotNull final Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Comment id=%d not found", id)));
    }

    public void delete(final Principal principal, @NotNull final Long id) {
        var comment = getById(id); // <-- checking if the given comment id is existing
        authorizationService.requireLogin(principal)
                .accessValidation(principal, comment.getUser().getId());
        commentRepository.delete(comment);
    }

}
