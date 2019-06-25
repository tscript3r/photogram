package pl.tscript3r.photogram.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        var result = new Comment(getDefaultUser(), getDefaultPost(), CONTENT);
        result.setId(ID);
        return result;
    }

    static Comment getSecondComment() {
        var result = new Comment(getSecondUser(), getSecondPost(), SECOND_CONTENT);
        result.setId(SECOND_ID);
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