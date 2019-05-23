package pl.tscript3r.photogram2.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
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
        result.setDislikes(DISLIKES);
        result.setCreationDate(CREATION_DATE);
        result.getComments().add(new Comment(getDefaultUser(), result, CONTENT)); // with getDefaultComment stackOverflow
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

    @Test
    @DisplayName("Setting imageId together with valid as true")
    void setImageIdAndValidAsTrue() {
        var post = getPostWithImageIdAsNull();
        post.setImageId(ID);
        assertEquals(ID, post.getImageId());
        assertTrue(post.getValid());
    }

    private Post getPostWithImageIdAsNull() {
        return new Post(getDefaultUser(), CAPTION, LOCATION);
    }

    @Test
    @DisplayName("Set null imageId (valid should be as false)")
    void setImageIdWithNullArgument() {
        var post = getPostWithImageIdAsNull();
        post.setImageId(null);
        assertNull(post.getImageId());
        assertFalse(post.getValid());
    }

    @Test
    @DisplayName("Set to low value imageId (valid should be as false)")
    void setImageIdWithToLowValueArgument() {
        var post = getPostWithImageIdAsNull();
        post.setImageId(-1L);
        assertNull(post.getImageId());
        assertFalse(post.getValid());
    }

    @Test
    @DisplayName("Increment likes")
    void incrementLikes() {
        var post = getDefaultPost();
        post.incrementLikes();
        assertEquals(2, post.getLikes().intValue());
    }

    @Test
    @DisplayName("Decrement likes")
    void incrementDislikes() {
        var post = getDefaultPost();
        post.incrementDislikes();
        assertEquals(2, post.getDislikes().intValue());
    }

    @Test
    @DisplayName("Decrement likes")
    void decrementLikes() {
        var post = getDefaultPost();
        post.decrementLikes();
        assertEquals(0, post.getLikes().intValue());
    }

    @Test
    @DisplayName("Decrement dislikes")
    void decrementDislikes() {
        var post = getDefaultPost();
        post.decrementDislikes();
        assertEquals(0, post.getDislikes().intValue());
    }

    @Test
    @DisplayName("Decrement likes equal 0")
    void decrementLikesEqualZero() {
        var post = getDefaultPost();
        post.decrementLikes();
        post.decrementLikes();
        assertEquals(0, post.getLikes().intValue());
    }

    @Test
    @DisplayName("Decrement dislikes equal 0")
    void decrementDislikesEqualZero() {
        var post = getDefaultPost();
        post.decrementDislikes();
        post.decrementDislikes();
        assertEquals(0, post.getDislikes().intValue());
    }

}