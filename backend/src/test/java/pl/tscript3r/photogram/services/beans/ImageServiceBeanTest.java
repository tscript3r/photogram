package pl.tscript3r.photogram.services.beans;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Image service")
class ImageServiceBeanTest {

    private ImageServiceBean imageService;

    @BeforeEach
    void init() {
        imageService = new ImageServiceBean();
    }

    @Test
    @DisplayName("Save post image")
    void savePostImage() {

    }
}