package pl.tscript3r.photogram.api.v1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.post.comment.CommentService;
import pl.tscript3r.photogram.post.comment.api.v1.CommentController;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDto;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDtoList;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram.api.v1.dtos.CommentDtoTest.getDefaultCommentDto;
import static pl.tscript3r.photogram.api.v1.dtos.CommentDtoTest.getSecondCommentDto;
import static pl.tscript3r.photogram.infrastructure.MappingsConsts.COMMENT_MAPPING;
import static pl.tscript3r.photogram.infrastructure.MappingsConsts.ID_POST_VARIABLE_MAPPING;

@DisplayName("Comment controller")
@WebMvcTest(CommentController.class)
@WithMockUser("spring")
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Get latest with default pageable")
    void getLatestByDefault() throws Exception {
        var inputCommentDtos = getInputCommentDtoSlice();
        when(commentService.getLatest(any(), any())).thenReturn(inputCommentDtos);
        var outputCommentDtos = getOutputCommentDtos(mockMvc.perform(MockMvcRequestBuilders.get(
                COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1") + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn());
        assertEquals(inputCommentDtos.getContent().size(), outputCommentDtos.getContent().size());
        verify(commentService, times(1)).getLatest(any(), any());
    }

    private Slice<CommentDto> getInputCommentDtoSlice() {
        return new SliceImpl<>(Arrays.asList(getDefaultCommentDto(), getSecondCommentDto()));
    }

    private CommentDtoList getOutputCommentDtos(MvcResult response) throws IOException {
        TypeReference<CommentDtoList> typeReference = new TypeReference<>() {
        };
        return objectMapper.readValue(response.getResponse().getContentAsString(), typeReference);
    }

    @Test
    @DisplayName("Get latest with custom pageable")
    void getLatestByCustomPageable() throws Exception {
        var inputCommentDtos = getInputCommentDtoSlice();
        when(commentService.getLatest(any(), any())).thenReturn(inputCommentDtos);
        var outputCommentDtos = getOutputCommentDtos(mockMvc.perform(MockMvcRequestBuilders.get(
                COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1") + "/1" + "?page=1&size=2&sort=id,desc")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn());
        assertEquals(inputCommentDtos.getContent().size(), outputCommentDtos.getContent().size());
        verify(commentService, times(1)).getLatest(any(), any());
    }

    @Test
    @DisplayName("Add valid")
    void add() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1"))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"content\":\"test\"}"))
                .andExpect(status().isCreated());
        verify(commentService, times(1)).save(any(), any(), any());
    }

    @Test
    @DisplayName("Add invalid")
    void addInvalid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1"))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{}"))
                .andExpect(status().isBadRequest());
        verify(commentService, times(0)).save(any(), any(), any());
    }

    @Test
    @DisplayName("Update existing")
    void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1") + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"content\":\"updated\"}"))
                .andExpect(status().isOk())
                .andReturn();

        verify(commentService, times(1)).update(any(), any(), any());
    }

    @Test
    @DisplayName("Update non existing")
    void updateNonExisting() throws Exception {
        when(commentService.update(any(), any(), any())).thenThrow(NotFoundPhotogramException.class);

        mockMvc.perform(MockMvcRequestBuilders.put(COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1") + "/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"content\":\"updated\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete existing comment when ")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(COMMENT_MAPPING.replace(ID_POST_VARIABLE_MAPPING, "1") + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}