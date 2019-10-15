package pl.tscript3r.photogram.post.api.v1;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.infrastructure.mapper.AbstractMapper;
import pl.tscript3r.photogram.infrastructure.mapper.CollectionMapper;
import pl.tscript3r.photogram.post.Post;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDto;
import pl.tscript3r.photogram.post.comment.api.v1.CommentMapper;
import pl.tscript3r.photogram.post.image.Image;
import pl.tscript3r.photogram.post.image.api.v1.ImageDto;
import pl.tscript3r.photogram.user.User;
import pl.tscript3r.photogram.user.UserService;

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
    public PostDto firstToSecond(final Post source) {
        PostDto result = new PostDto();
        result.setId(source.getId());
        result.setUserId(source.getUser().getId());
        result.setComments(commentMapper.map(source.getComments(), CommentDto.class));
        result.setCaption(source.getCaption());
        result.setLocation(source.getLocation());
        result.setVisibility(source.getVisibility());
        result.setImages(getImages(source.getImages()));
        result.setLikesCount(source.getLikes());
        result.setDislikesCount(source.getDislikes());
        result.setCreationDate(source.getCreationDate());
        result.setUsername(source.getUser().getUsername());
        result.setPostCount(source.getUser().getPosts().size());
        setLikedAndDislikedByCurrentUser(source, result);
        return result;
    }

    private void setLikedAndDislikedByCurrentUser(final Post post, final PostDto postDto) {
        var user = getLoggedUser();
        if (user != null) {
            postDto.setLiked(user.hasLikedPost(post));
            postDto.setDisliked(user.hasDislikedPost(post));
        }
    }

    private User getLoggedUser() {
        String username = getUsername();
        if (!username.isEmpty())
            return userService.getByUsername(username);
        else
            return null;
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken) && authentication != null)
            return authentication.getName();
        else
            return "";
    }

    private List<ImageDto> getImages(final List<Image> source) {
        var result = new ArrayList<ImageDto>();
        source.forEach(image -> result.add(new ImageDto(image.getImageId(), image.getExtension())));
        return result;
    }

    @Override
    public Post secondToFirst(final PostDto source) {
        User user = null;
        if (source.getUserId() != null)
            user = userService.getById(source.getUserId());
        return new Post(user, source.getCaption(), source.getLocation());
    }

}
