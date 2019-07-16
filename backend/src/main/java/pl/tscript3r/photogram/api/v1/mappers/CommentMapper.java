package pl.tscript3r.photogram.api.v1.mappers;

import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram.domains.Comment;

import javax.validation.constraints.NotNull;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDto> implements CollectionMapper {

    @Override
    protected CommentDto firstToSecond(@NotNull final Comment source) {
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
    protected Comment secondToFirst(@NotNull final CommentDto source) {
        return new Comment(source.getContent());
    }

}
