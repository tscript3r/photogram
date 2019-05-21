package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram2.domains.Comment;
import pl.tscript3r.photogram2.services.PostService;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.constraints.NotNull;

@Component
public class CommentMapper extends AbstractMapper<Comment, CommentDto> implements CollectionMapper {

    private final PostService postService;
    private final UserService userService;

    @Lazy
    public CommentMapper(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

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
        return new Comment(userService.getById(source.getUserId()),
                postService.getById(source.getPostId()), source.getContent());
    }

}
