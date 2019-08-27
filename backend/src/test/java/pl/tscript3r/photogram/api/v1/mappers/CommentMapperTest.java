package pl.tscript3r.photogram.api.v1.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.post.comment.Comment;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDto;
import pl.tscript3r.photogram.post.comment.api.v1.CommentMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.tscript3r.photogram.api.v1.dtos.CommentDtoTest.getDefaultCommentDto;
import static pl.tscript3r.photogram.domains.CommentTest.getDefaultComment;

@DisplayName("Comment mapper")
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

    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        commentMapper = new CommentMapper();
    }

    @Test
    @DisplayName("Comment to CommentDto map validation")
    void firstToSecond() {
        var commentDtoResult = commentMapper.firstToSecond(getDefaultComment());
        compareCommentWithCommentDto(getDefaultComment(), commentDtoResult);
    }

    @Test
    @DisplayName("CommentDto to Comment map validation")
    void secondToFirst() {
        var commentResult = commentMapper.secondToFirst(getDefaultCommentDto());
        compareCommentDtoWithComment(getDefaultCommentDto(), commentResult);
    }

}