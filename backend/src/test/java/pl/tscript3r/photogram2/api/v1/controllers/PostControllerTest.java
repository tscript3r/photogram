package pl.tscript3r.photogram2.api.v1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.exceptions.services.ForbiddenPhotogramException;
import pl.tscript3r.photogram2.exceptions.services.IgnoredPhotogramException;
import pl.tscript3r.photogram2.exceptions.services.NotFoundPhotogramException;
import pl.tscript3r.photogram2.services.PostService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.*;
import static pl.tscript3r.photogram2.api.v1.controllers.PostController.*;
import static pl.tscript3r.photogram2.api.v1.dtos.PostDtoTest.getDefaultPostDto;
import static pl.tscript3r.photogram2.api.v1.dtos.PostDtoTest.getSecondPostDto;

@DisplayName("Post controller")
@WebMvcTest(PostController.class)
@WithMockUser("spring")
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Get latest without parameters")
    void getLatestWithoutParameters() throws Exception {
        var inputPostDtos = getInputPostDtoList();
        when(postService.getLatest(any())).thenReturn(inputPostDtos);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(POST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TypeReference<List<PostDto>> typeReference = new TypeReference<>() {
        };
        List<PostDto> outputPostDtos = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
        assertEquals(inputPostDtos.size(), outputPostDtos.size());
        verify(postService, times(1)).getLatest(any());
    }

    private List<PostDto> getInputPostDtoList() {
        return Arrays.asList(getDefaultPostDto(), getSecondPostDto());
    }

    @Test
    @DisplayName("Get latest with username param")
    void getLatestWithUserParam() throws Exception {
        var inputPostDtos = getInputPostDtoList();
        when(postService.getLatest(any(), any())).thenReturn(inputPostDtos);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(POST_MAPPING + "?" + USERNAME_PARAM + "=any")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TypeReference<List<PostDto>> typeReference = new TypeReference<>() {
        };
        List<PostDto> outputPostDtos = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
        assertEquals(inputPostDtos.size(), outputPostDtos.size());
        verify(postService, times(1)).getLatest(any(), any());
    }

    @Test
    @DisplayName("Get latest with count param")
    void getLatestWithCountParam() throws Exception {
        var inputPostDtos = getInputPostDtoList();
        when(postService.getLatest(any())).thenReturn(inputPostDtos);
        mockMvc.perform(MockMvcRequestBuilders.get(POST_MAPPING + "?" + COUNT_PARAM + "=666")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        verify(postService, times(1)).getLatest(666);
    }

    @Test
    @DisplayName("Get latest users own posts")
    void getLatestUsersOwnPosts() throws Exception {
        var inputPostDtos = getInputPostDtoList();
        when(postService.getLatestUsersPosts(any(), any())).thenReturn(inputPostDtos);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(POST_MAPPING + "?" + OWN_PARAM + "=true")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TypeReference<List<PostDto>> typeReference = new TypeReference<>() {
        };
        List<PostDto> outputPostDtos = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
        assertEquals(inputPostDtos.size(), outputPostDtos.size());
        verify(postService, times(1)).getLatestUsersPosts(any(), any());
    }

    @Test
    @DisplayName("Get latest with bad request - own & username param")
    void getLatestWithOwnAndUsernameParamsException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(POST_MAPPING + "?" + OWN_PARAM + "=true" + "&"
                + USERNAME_PARAM + "=any")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Add valid post")
    void addValidPost() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setCaption(CAPTION);
        postDto.setLocation(LOCATION);
        mockMvc.perform(MockMvcRequestBuilders.post(POST_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isCreated());
        verify(postService, times(1)).save(any(), any());
    }

    @Test
    @DisplayName("Add valid (without parameters) post")
    void addValidPostWithoutParameters() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(POST_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isCreated());
        verify(postService, times(1)).save(any(), any());
    }

    @Test
    @DisplayName("Update post")
    void updatePost() throws Exception {
        PostDto postDto = new PostDto();
        mockMvc.perform(MockMvcRequestBuilders.put(POST_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk());
        verify(postService, times(1)).update(any(), any());
    }

    @Test
    @DisplayName("Update other users post - access denied")
    void updateOtherUsersPostException() throws Exception {
        PostDto postDto = new PostDto();
        when(postService.update(any(), any())).thenThrow(ForbiddenPhotogramException.class);
        mockMvc.perform(MockMvcRequestBuilders.put(POST_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete existing post")
    void deleteExistingPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(POST_MAPPING + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(postService, times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("Delete non existing post")
    void deleteNonExistingPost() throws Exception {
        doThrow(NotFoundPhotogramException.class).when(postService).delete(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.delete(POST_MAPPING + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(postService, times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("Delete post owned by other user exception")
    void deletePostFromOtherUserException() throws Exception {
        doThrow(ForbiddenPhotogramException.class).when(postService).delete(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.delete(POST_MAPPING + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        verify(postService, times(1)).delete(any(), any());
    }

    @Test
    @DisplayName("Like existing post")
    void likeExistingPost() throws Exception {
        mockMvcPerform(LIKE_MAPPING, status().isOk());
        verify(postService, times(1)).like(any(), any());
    }

    private void mockMvcPerform(String mappingSuffix, ResultMatcher expectedResult) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(POST_MAPPING + "/" + ID
                + "/" + mappingSuffix);
        mockMvc.perform(request.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(expectedResult);
    }

    @Test
    @DisplayName("Like previous liked post")
    void likePreviousLikedPost() throws Exception {
        when(postService.like(any(), any())).thenThrow(IgnoredPhotogramException.class);
        mockMvcPerform(LIKE_MAPPING, status().isContinue());
        verify(postService, times(1)).like(any(), any());
    }

    @Test
    @DisplayName("Like unexisting post")
    void likeUnexistingPost() throws Exception {
        when(postService.like(any(), any())).thenThrow(NotFoundPhotogramException.class);
        mockMvcPerform(LIKE_MAPPING, status().isNotFound());
        verify(postService, times(1)).like(any(), any());
    }

    @Test
    @DisplayName("Like without principal")
    void likeWithoutPrincipal() throws Exception {
        when(postService.like(any(), any())).thenThrow(ForbiddenPhotogramException.class);
        mockMvcPerform(LIKE_MAPPING, status().isForbidden());
        verify(postService, times(1)).like(any(), any());
    }

    @Test
    @DisplayName("Unlike existing previous liked post")
    void unlikeExistingPreviousLikedPost() throws Exception {
        mockMvcPerform(UNLIKE_MAPPING, status().isOk());
        verify(postService, times(1)).unlike(any(), any());
    }

    @Test
    @DisplayName("Unlike existing non liked post")
    void unlikeExistingNonLikedPost() throws Exception {
        when(postService.unlike(any(), any())).thenThrow(IgnoredPhotogramException.class);
        mockMvcPerform(UNLIKE_MAPPING, status().isContinue());
        verify(postService, times(1)).unlike(any(), any());
    }

    @Test
    @DisplayName("Unlike non existing post")
    void unlikeNonExistingPost() throws Exception {
        when(postService.unlike(any(), any())).thenThrow(NotFoundPhotogramException.class);
        mockMvcPerform(UNLIKE_MAPPING, status().isNotFound());
        verify(postService, times(1)).unlike(any(), any());
    }

    @Test
    @DisplayName("Unlike without principal")
    void unlikeWithoutPrincipal() throws Exception {
        when(postService.unlike(any(), any())).thenThrow(ForbiddenPhotogramException.class);
        mockMvcPerform(UNLIKE_MAPPING, status().isForbidden());
        verify(postService, times(1)).unlike(any(), any());
    }

    @Test
    @DisplayName("Dislike existing post")
    void dislikeExistingPost() throws Exception {
        mockMvcPerform(DISLIKE_MAPPING, status().isOk());
        verify(postService, times(1)).dislike(any(), any());
    }

    @Test
    @DisplayName("Dislike previous disliked post")
    void dislikePreviousDislikedPost() throws Exception {
        when(postService.dislike(any(), any())).thenThrow(IgnoredPhotogramException.class);
        mockMvcPerform(DISLIKE_MAPPING, status().isContinue());
        verify(postService, times(1)).dislike(any(), any());
    }

    @Test
    @DisplayName("Dislike unexisting post")
    void dislikeUnexistingPost() throws Exception {
        when(postService.dislike(any(), any())).thenThrow(NotFoundPhotogramException.class);
        mockMvcPerform(DISLIKE_MAPPING, status().isNotFound());
        verify(postService, times(1)).dislike(any(), any());
    }

    @Test
    @DisplayName("Dislike without principal")
    void dislikeWithoutPrincipal() throws Exception {
        when(postService.dislike(any(), any())).thenThrow(ForbiddenPhotogramException.class);
        mockMvcPerform(DISLIKE_MAPPING, status().isForbidden());
        verify(postService, times(1)).dislike(any(), any());
    }

    @Test
    @DisplayName("Undislike existing previous liked post")
    void undislikeExistingPreviousLikedPost() throws Exception {
        mockMvcPerform(UNDISLIKE_MAPPING, status().isOk());
        verify(postService, times(1)).undislike(any(), any());
    }

    @Test
    @DisplayName("Undislike existing non liked post")
    void undislikeExistingNonLikedPost() throws Exception {
        when(postService.undislike(any(), any())).thenThrow(IgnoredPhotogramException.class);
        mockMvcPerform(UNDISLIKE_MAPPING, status().isContinue());
        verify(postService, times(1)).undislike(any(), any());
    }

    @Test
    @DisplayName("Undislike non existing post")
    void undislikeNonExistingPost() throws Exception {
        when(postService.undislike(any(), any())).thenThrow(NotFoundPhotogramException.class);
        mockMvcPerform(UNDISLIKE_MAPPING, status().isNotFound());
        verify(postService, times(1)).undislike(any(), any());
    }

    @Test
    @DisplayName("Undislike without principal")
    void undislikeWithoutPrincipal() throws Exception {
        when(postService.undislike(any(), any())).thenThrow(ForbiddenPhotogramException.class);
        mockMvcPerform(UNDISLIKE_MAPPING, status().isForbidden());
        verify(postService, times(1)).undislike(any(), any());
    }

}