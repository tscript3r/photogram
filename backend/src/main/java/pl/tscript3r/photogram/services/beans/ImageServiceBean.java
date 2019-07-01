package pl.tscript3r.photogram.services.beans;

import org.apache.commons.io.FileUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.domains.Image;
import pl.tscript3r.photogram.exceptions.InternalErrorPhotogramException;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.services.ImageService;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageServiceBean implements ImageService {

    static final Long BASE_IMAGE_ID = 1000L;

    private static final String BASE_PATH = "backend/src/main/resources/";
    static final String POST_IMAGES_PATH = BASE_PATH + "posts/%d/images/";
    private static final String USER_AVATAR_PATH = BASE_PATH + "users/%d/avatar";
    private static final String DEFAULT_AVATAR_PATH = BASE_PATH + "users/default_avatar.png";

    @Override
    public void savePostImage(@NotNull final Long postId, @NotNull final Image image,
                              @NotNull final MultipartFile multipartFile) {
        try {
            writeFile(getPostPath(postId) + getFileName(image), multipartFile.getInputStream());
        } catch (IOException e) {
            throw getSaveInternalErrorException(e);
        }
    }

    private InternalErrorPhotogramException getSaveInternalErrorException(Exception e) {
        return new InternalErrorPhotogramException("Something went wrong by saving the image: " + e, e);
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

    @Override
    public ResponseEntity<byte[]> getPostImage(@NotNull final Long postId, @NotNull final Image image) {
        return loadFile(getPostPath(postId) + getFileName(image), getHeader(image.getExtension()));
    }

    private ResponseEntity<byte[]> loadFile(final String filePath, final HttpHeaders header) {
        try {
            File imageFile = new File(filePath);
            if (imageFile.exists()) {
                byte[] imageBytes = readToByteArray(imageFile);
                return new ResponseEntity<>(imageBytes, header, HttpStatus.OK);
            } else
                throw new NotFoundPhotogramException(String.format("Image could not be found [path=%s]", filePath));
        } catch (IOException e) {
            throw new InternalErrorPhotogramException(String.format("Image could not be read [path=%s]: ", filePath), e);
        }
    }

    private byte[] readToByteArray(final File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    private HttpHeaders getHeader(final String imageExtension) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(getMediaType(imageExtension));
        return headers;
    }

    private MediaType getMediaType(final String imageExtension) {
        // TODO: refactor
        return MediaType.IMAGE_JPEG;
    }

    @Override
    public ResponseEntity<byte[]> getAvatar(@NotNull final Long userId) {
        File usersAvatar = new File(getUserAvatarPath(userId));
        if (usersAvatar.exists())
            return loadFile(usersAvatar.getPath(), getHeader(""));
        else
            return getDefaultAvatar();
    }

    private String getUserAvatarPath(final Long userId) {
        return String.format(USER_AVATAR_PATH, userId);
    }

    private ResponseEntity<byte[]> getDefaultAvatar() {
        return loadFile(DEFAULT_AVATAR_PATH, getHeader("png"));
    }

    @Override
    public void saveAvatar(@NotNull final Long userId, @NotNull final MultipartFile multipartFile) {
        try {
            writeFile(getUserAvatarPath(userId), multipartFile.getInputStream());
        } catch (IOException e) {
            throw getSaveInternalErrorException(e);
        }
    }

}
