package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram2.api.v1.dtos.ImageDto;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.domains.Image;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.services.UserService;

import java.util.ArrayList;
import java.util.List;


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
        result.setVisibility(source.getVisibility());
        result.setImages(getImages(source.getImages()));
        result.setLikesCount(source.getLikes());
        result.setCreationDate(source.getCreationDate());
        return result;
    }

    private List<ImageDto> getImages(final List<Image> source) {
        var result = new ArrayList<ImageDto>();
        source.forEach(image -> result.add(new ImageDto(image.getImageId(), image.getExtension())));
        return result;
    }

    @Override
    protected Post secondToFirst(final PostDto source) {
        User user = null;
        if (source.getUserId() != null)
            user = userService.getById(source.getUserId());
        return new Post(user, source.getCaption(), source.getLocation());
    }

}
