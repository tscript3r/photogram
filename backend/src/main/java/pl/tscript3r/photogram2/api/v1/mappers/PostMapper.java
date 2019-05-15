package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.services.UserService;

@Component
public class PostMapper extends AbstractMapper<Post, PostDto> implements CollectionMapper {

    private final UserService userService;

    @Lazy
    public PostMapper(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected PostDto firstToSecond(final Post source) {
        PostDto result = new PostDto();
        result.setId(source.getId());
        result.setUserId(source.getUser().getId());
        // TODO: Add comment mapping
        result.setCaption(source.getCaption());
        result.setLocation(source.getLocation());
        result.setImageId(source.getImageId());
        result.setLikesCount(source.getLikes());
        result.setCreationDate(source.getCreationDate());
        return result;
    }

    @Override
    protected Post secondToFirst(final PostDto source) {
        Post result = new Post();
        result.setUser(
                userService.getById(source.getUserId())
        );
        result.setCaption(source.getCaption());
        result.setLocation(source.getLocation());
        return result;
    }

}
