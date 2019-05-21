package pl.tscript3r.photogram2.api.v1.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram2.domains.Comment;
import pl.tscript3r.photogram2.services.PostService;
import pl.tscript3r.photogram2.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram2.api.v1.dtos.CommentDtoTest.getDefaultCommentDto;
import static pl.tscript3r.photogram2.domains.CommentTest.getDefaultComment;
import static pl.tscript3r.photogram2.domains.PostTest.getDefaultPost;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;

@DisplayName("Comment mapper")
@ExtendWith(MockitoExtension.class)
class CommentMapperTest {

    static void compareCommentWithCommentDto(Comment comment, CommentDto commentDto) {
        assertEquals(comment.getId(), commentDto.getId());
        assertEquals(comment.getUser().getId(), commentDto.getUserId());
        assertEquals(comment.getUser().getUsername(), commentDto.getUsername());
        assertEquals(comment.getContent(), commentDto.getContent());
        assertEquals(comment.getCreationDate(), commentDto.getCreationDate());
        assertEquals(comment.getPost().getId(), commentDto.getPostId());
    }

    static void compareCommentDtoWithComment(CommentDto commentDto, Comment comment) {
        assertEquals(commentDto.getUserId(), commentDto.getUserId());
        assertEquals(commentDto.getUsername(), commentDto.getUsername());
        assertEquals(commentDto.getContent(), commentDto.getContent());
        assertEquals(commentDto.getCreationDate(), commentDto.getCreationDate());
        assertEquals(commentDto.getPostId(), commentDto.getPostId());

    }

    @Mock
    UserService userService;

    @Mock
    PostService postService;

    @InjectMocks
    CommentMapper commentMapper;

    @Test
    @DisplayName("Comment to CommentDto map validation")
    void firstToSecond() {
        var commentDtoResult = commentMapper.firstToSecond(getDefaultComment());
        compareCommentWithCommentDto(getDefaultComment(), commentDtoResult);
    }

    @Test
    @DisplayName("CommentDto to Comment map validation")
    void secondToFirst() {
        when(userService.getById(any())).thenReturn(getDefaultUser());
        when(postService.getById(any())).thenReturn(getDefaultPost());
        var commentResult = commentMapper.secondToFirst(getDefaultCommentDto());
        compareCommentDtoWithComment(getDefaultCommentDto(), commentResult);
    }

}