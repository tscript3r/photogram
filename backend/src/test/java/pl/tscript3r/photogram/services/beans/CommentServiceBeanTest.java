package pl.tscript3r.photogram.services.beans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import pl.tscript3r.photogram.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram.api.v1.services.MapperService;
import pl.tscript3r.photogram.domains.Comment;
import pl.tscript3r.photogram.domains.DataStructure;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.repositories.CommentRepository;
import pl.tscript3r.photogram.services.AuthorizationService;
import pl.tscript3r.photogram.services.PostService;
import pl.tscript3r.photogram.services.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram.Consts.ID;
import static pl.tscript3r.photogram.Consts.USERNAME;
import static pl.tscript3r.photogram.api.v1.dtos.CommentDtoTest.getDefaultCommentDto;
import static pl.tscript3r.photogram.api.v1.dtos.CommentDtoTest.getSecondCommentDto;
import static pl.tscript3r.photogram.domains.CommentTest.getDefaultComment;
import static pl.tscript3r.photogram.domains.PostTest.getDefaultPost;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;

@DisplayName("Comment service")
@ExtendWith(MockitoExtension.class)
class CommentServiceBeanTest {

    @Mock
    PostService postService;

    @Mock
    UserService userService;

    @Mock
    MapperService mapperService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    AuthorizationService authorizationService;

    @Mock
    Pageable pageable;

    @InjectMocks
    CommentServiceBean commentService;

    @Test
    @DisplayName("Get latest")
    void getLatest() {
        when(commentRepository.findByPostId(any(), any())).thenReturn(getDomainSlice());
        when(mapperService.map(anyCollection(), any())).thenReturn(getDtoList());
        assertEquals(1, commentService.getLatest(ID, pageable).getContent().size());
    }

    private Slice<Comment> getDomainSlice() {
        return new SliceImpl<>(Collections.singletonList(getDefaultComment()));
    }

    private List<CommentDto> getDtoList() {
        return Collections.singletonList(getDefaultCommentDto());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Save")
    void save() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(authorizationService.accessValidation(any(), any())).thenReturn(Optional.of(getDefaultUser()));
        when(mapperService.map(any(CommentDto.class), any())).thenReturn(getDefaultComment());
        when(postService.getById(any())).thenReturn(getDefaultPost());
        when(mapperService.map(any(Comment.class), any())).thenReturn(getDefaultCommentDto());

        commentService.save(() -> USERNAME, ID, getSecondCommentDto());

        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
        verify(mapperService, times(1)).map(any(DataStructure.class), any());
        verify(postService, times(1)).getById(any());
    }

    @Test
    @DisplayName("Update")
    void update() {
        var comment = getDefaultComment();
        var commentDto = getSecondCommentDto();
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(authorizationService.accessValidation(any(), any())).thenReturn(Optional.of(getDefaultUser()));
        when(commentRepository.save(any())).thenReturn(comment);
        when(mapperService.map(any(DataStructure.class), any())).thenReturn(commentDto);

        assertNotNull(commentService.update(() -> USERNAME, ID, commentDto));

        assertEquals(commentDto.getContent(), comment.getContent());
    }

    @Test
    @DisplayName("Update non existing comment")
    void updateNonExisting() {
        when(commentRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () ->
                commentService.update(() -> USERNAME, ID, getDefaultCommentDto()));
    }

    @Test
    @DisplayName("Delete")
    void delete() {
        when(commentRepository.findById(any())).thenReturn(Optional.of(getDefaultComment()));
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(authorizationService.accessValidation(any(), any())).thenReturn(Optional.of(getDefaultUser()));

        commentService.delete(() -> USERNAME, ID);

        verify(commentRepository, times(1)).delete(any());
    }

}