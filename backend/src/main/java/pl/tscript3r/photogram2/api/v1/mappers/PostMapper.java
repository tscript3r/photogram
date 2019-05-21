package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.services.UserService;


@Component
public class PostMapper extends AbstractMapper<Post, PostDto> implements CollectionMapper {

    private final UserService userService;
    private final CommentMapper commentMapper;

    @Lazy
    public PostMapper(UserService userService, CommentMapper commentMapper) {
        this.userService = userService;
        this.commentMapper = commentMapper;
    }

    @Override
    protected PostDto firstToSecond(final Post source) {
        PostDto result = new PostDto();
        result.setId(source.getId());
        result.setUserId(source.getUser().getId());
        result.setComments(commentMapper.map(source.getComments(), CommentDto.class));
        result.setCaption(source.getCaption());
        result.setLocation(source.getLocation());
        result.setImageId(source.getImageId());
        result.setLikesCount(source.getLikes());
        result.setCreationDate(source.getCreationDate());
        return result;
    }

    @Override
    protected Post secondToFirst(final PostDto source) {
        return new Post(userService.getById(source.getUserId()), source.getCaption(), source.getLocation());
    }

}
