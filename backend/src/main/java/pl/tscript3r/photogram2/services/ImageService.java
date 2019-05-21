package pl.tscript3r.photogram2.services;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public interface ImageService {

    void setAvatar(@NotNull final Long userId, @NotNull final Long imageId);

    /**
     * @param userId        id of the user
     * @param postId        id of the image owner post
     * @param multipartFile received image file
     * @return id of the saved file
     */
    Long save(@NotNull final Long userId, @NotNull final Long postId, @NotNull final MultipartFile multipartFile);

    Long reserveNextImageId(@NotNull final Long userId);

    MultipartFile loadAvatar(@NotNull final Long userId);

    MultipartFile load(@NotNull final Long userId, @NotNull final Long imageId);

    void delete(@NotNull final Long userId, @NotNull final Long imageId);

}
