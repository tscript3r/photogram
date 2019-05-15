package pl.tscript3r.photogram2.api.v1.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram2.api.v1.dtos.PostDtoTest.getDefaultPostDto;
import static pl.tscript3r.photogram2.domains.PostTest.getDefaultPost;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;

@DisplayName("Post mapper")
@ExtendWith(MockitoExtension.class)
class PostMapperTest {

    void comparePostWithPostDto(Post post, PostDto postDto) {
        assertEquals(post.getId(), postDto.getId());
        assertEquals(post.getUser().getId(), postDto.getUserId());
        assertEquals(post.getCaption(), postDto.getCaption());
        assertEquals(post.getLocation(), postDto.getLocation());
        assertEquals(post.getImageId(), postDto.getImageId());
        assertEquals(post.getLikes(), postDto.getLikesCount());
        assertEquals(post.getCreationDate(), postDto.getCreationDate());
    }

    void comparePostDtoWithPost(PostDto postDto, Post post) {
        assertEquals(post.getUser().getId(), postDto.getUserId());
        assertEquals(post.getCaption(), postDto.getCaption());
        assertEquals(post.getLocation(), postDto.getLocation());
    }

    @Mock
    UserService userService;

    @InjectMocks
    PostMapper postMapper;

    @Test
    @DisplayName("Post to PostDto map validation")
    void firstToSecond() {
        var post = getDefaultPost();
        var postDto = postMapper.firstToSecond(post);
        comparePostWithPostDto(post, postDto);
    }

    @Test
    @DisplayName("PostDto to Post map validation")
    void secondToFirst() {
        var postDto = getDefaultPostDto();
        when(userService.getById(any())).thenReturn(getDefaultUser());
        var post = postMapper.secondToFirst(postDto);
        comparePostDtoWithPost(postDto, post);
    }

}