package pl.tscript3r.photogram.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.post.comment.Comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.domains.PostTest.getDefaultPost;
import static pl.tscript3r.photogram.domains.PostTest.getSecondPost;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram.domains.UserTest.getSecondUser;

@DisplayName("Comment")
public
class CommentTest {

    public static Comment getDefaultComment() {
        var result = new Comment(CONTENT);
        result.setId(ID);
        result.setUser(getDefaultUser());
        result.setPost(getDefaultPost());
        return result;
    }

    static Comment getSecondComment() {
        var result = new Comment(SECOND_CONTENT);
        result.setId(SECOND_ID);
        result.setUser(getSecondUser());
        result.setPost(getSecondPost());
        return result;
    }

    @Test
    @DisplayName("Equals only with same ID")
    void commentEqualsWithId() {
        var secondUser = getSecondComment();
        secondUser.setId(ID);
        assertEquals(getDefaultComment(), secondUser);
    }

    @Test
    @DisplayName("Not equals with only different ID")
    void commentNotEqualsWithId() {
        var defaultUser = getDefaultComment();
        defaultUser.setId(SECOND_ID);
        assertNotEquals(getDefaultComment(), defaultUser);
    }

}