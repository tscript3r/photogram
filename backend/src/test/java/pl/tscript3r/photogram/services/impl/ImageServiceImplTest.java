package pl.tscript3r.photogram.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.tscript3r.photogram.services.impl.ImageServiceImpl.BASE_IMAGE_ID;
import static pl.tscript3r.photogram.services.impl.ImageServiceImpl.POST_IMAGES_PATH;

@DisplayName("Image service")
class ImageServiceImplTest {

    private ImageServiceImpl imageService;

    @BeforeEach
    void init() {
        imageService = new ImageServiceImpl();
    }

    @Test
    void save() {
    }

    @Test
    void exists() {
    }

    @Test
    @DisplayName("Get first image ID")
    void getNextId() {
        assertEquals(BASE_IMAGE_ID, imageService.getNextId(1L));
    }

    @Test
    @DisplayName("Get second image ID")
    void getSecondImageId() throws IOException {
        Path temporaryFilePath = createTemporaryDirectory().resolve(BASE_IMAGE_ID.toString());
        createTemporaryFile(temporaryFilePath);
        assertEquals(BASE_IMAGE_ID + 1L, imageService.getNextId(0L).longValue());
        cleanUpDirectoriesAndFile(temporaryFilePath);
    }

    private Path createTemporaryDirectory() {
        Path temporaryDirectoryPath = Paths.get(String.format(POST_IMAGES_PATH, 0));
        File temporaryDirectoryFile = new File(String.valueOf(temporaryDirectoryPath));
        assertTrue(temporaryDirectoryFile.mkdirs());
        return temporaryDirectoryPath;
    }

    private void createTemporaryFile(Path path) throws IOException {
        File temporaryFile = new File(path.toUri());
        assertTrue(temporaryFile.createNewFile());
    }

    private void cleanUpDirectoriesAndFile(Path path) throws IOException {
        Files.delete(path);
        Files.delete(path.getParent());
        Files.delete(path.getParent().getParent());
    }

}