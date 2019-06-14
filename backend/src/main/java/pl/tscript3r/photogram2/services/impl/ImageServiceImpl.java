package pl.tscript3r.photogram2.services.impl;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram2.domains.Image;
import pl.tscript3r.photogram2.exceptions.InternalErrorPhotogramException;
import pl.tscript3r.photogram2.services.ImageService;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceImpl implements ImageService {

    static final Long BASE_IMAGE_ID = 1000L;

    private static final String BASE_PATH = "backend/src/main/resources/";
    static final String POST_IMAGES_PATH = BASE_PATH + "posts/%d/images/";
    private static final String USER_AVATAR_PATH = BASE_PATH + "users/%d/%d";

    @Override
    public void save(@NotNull final Long postId, final Image image, @NotNull final MultipartFile multipartFile) {
        try {
            writeFile(getPostPath(postId) + getFileName(image), multipartFile.getInputStream());
        } catch (IOException e) {
            throw new InternalErrorPhotogramException("Something went wrong by saving the image: " + e, e);
        }
    }

    private void writeFile(final String savePath, final InputStream imageInputStream) throws IOException {
        File targetFile = new File(savePath);
        FileUtils.copyInputStreamToFile(imageInputStream, targetFile);
    }

    private String getPostPath(final Long postId) {
        return String.format(POST_IMAGES_PATH, postId);
    }

    private String getFileName(final Image image) {
        return image.getFileName();
    }

    @Override
    public Boolean exists(@NotNull final Long postId, @NotNull final Image image) {
        File directory = new File(getPostPath(postId));
        File[] imagesList = directory.listFiles();
        if (imagesList != null)
            for (File file : imagesList)
                if (file.getName().equalsIgnoreCase(getFileName(image)))
                    return true;
        return false;
    }

    @Override
    public Long getNextId(@NotNull final Long postId) {
        Path path = Paths.get(getPostPath(postId));
        long searchedId = BASE_IMAGE_ID;
        while (isExisting(path, searchedId))
            searchedId++;
        return searchedId;
    }

    private Boolean isExisting(final Path path, final Long id) {
        return Files.exists(path.resolve(id.toString()));
    }

}
