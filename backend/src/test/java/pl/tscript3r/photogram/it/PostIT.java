package pl.tscript3r.photogram.it;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import pl.tscript3r.photogram.api.v1.dtos.PostDto;
import pl.tscript3r.photogram.domains.Visibility;
import pl.tscript3r.photogram.services.ImageService;
import pl.tscript3r.photogram.services.PostService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.api.v1.controllers.MappingsConsts.POST_MAPPING;
import static pl.tscript3r.photogram.api.v1.controllers.PostController.LIKE_MAPPING;
import static pl.tscript3r.photogram.api.v1.controllers.PostController.UNLIKE_MAPPING;
import static pl.tscript3r.photogram.services.PostService.Reactions.LIKE;
import static pl.tscript3r.photogram.services.PostService.Reactions.UNLIKE;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser("admin")
public class PostIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageService imageService;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper.configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Test
    @DisplayName("Get by ID")
    void getByID() throws Exception {
        var tmpObjectMapper = new ObjectMapper();
        tmpObjectMapper.configure(MapperFeature.USE_ANNOTATIONS, true);
        var mockMvcResult = mockMvc.perform(get(POST_MAPPING + "/" + ID)).andExpect(status().isOk()).andReturn();
        PostDto postDtoResult = tmpObjectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), PostDto.class);
        assertEquals(postDtoResult.getId(), ID);
    }

    @Test
    @DisplayName("Add empty post")
    void addPost() throws Exception {
        var result = addPostWithJson("{}");
        assertNotNull(result.getId());
        assertNotNull(result.getUserId());
        assertEquals(Visibility.PRIVATE, result.getVisibility());
    }

    private PostDto addPostWithJson(String json) throws Exception {
        var mockMvcResult = mockMvc.perform(post(POST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated()).andReturn();
        return objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), PostDto.class);
    }

    @Test
    @DisplayName("Add post with caption & location")
    void addPostWithValues() throws Exception {
        var result = addPostWithJson("{\n" +
                "\t\"caption\": \"" + CAPTION + "\",\n" +
                "\t\"location\": \"" + LOCATION + "\"\n" +
                "}");
        assertEquals(CAPTION, result.getCaption());
        assertEquals(LOCATION, result.getLocation());
    }

    @Test
    @DisplayName("Update post caption & location")
    void updatePostCaptionAndLocation() throws Exception {
        var result = addPostWithJson("{}");
        assertNull(result.getLocation());
        assertNull(result.getCaption());
        assertNotNull(result.getId());

        var mockMvcResult = mockMvc.perform(post(POST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"caption\": \"" + CAPTION + "\",\n" +
                        "\t\"location\": \"" + LOCATION + "\"\n" +
                        "}"))
                .andExpect(status().isCreated()).andReturn();
        var updatedResult = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), PostDto.class);

        assertEquals(CAPTION, updatedResult.getCaption());
        assertEquals(LOCATION, updatedResult.getLocation());
    }

    @Test
    @DisplayName("Delete post")
    void deletePost() throws Exception {
        var result = addPostWithJson("{}");
        mockMvc.perform(delete(POST_MAPPING + "/" + result.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get(POST_MAPPING + "/" + result.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Like post")
    void likePost() throws Exception {
        assertEquals(1, react(addPostWithJson("{}").getId(), true, LIKE_MAPPING, LIKE, status().isOk()));
    }

    private int react(Long postId, Boolean mapResults, String suffixMapping, PostService.Reactions returnByReaction,
                      ResultMatcher expectedStatus)
            throws Exception {

        var mockMvcResult = mockMvc.perform(put(POST_MAPPING + "/" + postId + "/" + suffixMapping))
                .andExpect(expectedStatus)
                .andReturn();
        if (mapResults) {
            var reactedPost = objectMapper.readValue(mockMvcResult.getResponse().getContentAsString(), PostDto.class);
            if (returnByReaction == LIKE || returnByReaction == UNLIKE)
                return reactedPost.getLikesCount();
            else
                return reactedPost.getDislikesCount();
        } else
            return 0;
    }

    @Test
    @DisplayName("Redundant post like")
    void redundantLikePost() throws Exception {
        var post = addPostWithJson("{}");
        assertEquals(1, react(post.getId(), true, LIKE_MAPPING, LIKE, status().isOk()));
        react(post.getId(), false, LIKE_MAPPING, LIKE, status().isContinue());
    }

    @Test
    @DisplayName("Unlike post")
    void unlikePost() throws Exception {
        var post = addPostWithJson("{}");
        assertEquals(1, react(post.getId(), true, LIKE_MAPPING, LIKE, status().isOk()));
        assertEquals(0, react(post.getId(), true, UNLIKE_MAPPING, UNLIKE, status().isOk()));
    }

    @Test
    @DisplayName("Unlike not liked post")
    void unlikeNotLikedPost() throws Exception {
        var post = addPostWithJson("{}");
        react(post.getId(), false, UNLIKE_MAPPING, UNLIKE, status().isContinue());
    }

}
