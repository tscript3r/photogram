package pl.tscript3r.photogram.services.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.post.image.ImageService;

@DisplayName("Image service")
class ImageServiceTest {

    private ImageService imageService;

    @BeforeEach
    void init() {
        imageService = new ImageService();
    }

    @Test
    @DisplayName("Save post image")
    void savePostImage() {

    }
}