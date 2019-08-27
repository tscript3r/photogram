package pl.tscript3r.photogram.post.comment.api.v1;

import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.infrastructure.mapper.AbstractMapper;
import pl.tscript3r.photogram.infrastructure.mapper.CollectionMapper;
import pl.tscript3r.photogram.post.comment.Comment;

import javax.validation.constraints.NotNull;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDto> implements CollectionMapper {

    @Override
    public CommentDto firstToSecond(@NotNull final Comment source) {
        var result = new CommentDto();
        result.setId(source.getId());
        result.setUserId(source.getUser().getId());
        result.setUsername(source.getUser().getUsername());
        result.setContent(source.getContent());
        result.setCreationDate(source.getCreationDate());
        result.setPostId(source.getPost().getId());
        return result;
    }

    @Override
    public Comment secondToFirst(@NotNull final CommentDto source) {
        return new Comment(source.getContent());
    }

}
