package pl.tscript3r.photogram2.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram2.domains.UserTest.getSecondUser;

@DisplayName("Post")
public class PostTest {

    public static Post getDefaultPost() {
        var result = new Post(getDefaultUser(), CAPTION, LOCATION);
        result.setId(ID);
        result.setImageId(IMAGE_ID);
        result.setLikes(LIKES);
        result.setCreationDate(CREATION_DATE);
        return result;
    }

    public static Post getSecondPost() {
        var result = new Post(getSecondUser(), SECOND_CAPTION, SECOND_LOCATION);
        result.setId(SECOND_ID);
        result.setImageId(SECOND_IMAGE_ID);
        result.setLikes(SECOND_LIKES);
        result.setCreationDate(SECOND_CREATION_DATE);
        return result;
    }

    @Test
    @DisplayName("Equals only with same ID")
    void equals() {
        var post = getDefaultPost();
        var secondPost = getSecondPost();
        secondPost.setId(post.getId());
        assertEquals(post, secondPost);
    }

    @Test
    @DisplayName("Not equals with only different ID")
    void notEqualsWithDifferentId() {
        var post = getDefaultPost();
        var almostSamePost = getDefaultPost();
        almostSamePost.setId(post.getId() + 1L);
        assertNotEquals(post, almostSamePost);
    }

}