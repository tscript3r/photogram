package pl.tscript3r.photogram.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.post.image.Image;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.tscript3r.photogram.Consts.*;

@DisplayName("Image")
class ImageTest {

    public static Image getDefaultImage() {
        var image = new Image(IMAGE_ID, IMAGE_EXTENSION);
        image.setId(ID);
        return image;
    }

    @Test
    @DisplayName("Get file name")
    void getFileName() {
        assertEquals(IMAGE_ID.toString(), getDefaultImage().getFileName());
    }

}