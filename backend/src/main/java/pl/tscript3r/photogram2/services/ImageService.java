package pl.tscript3r.photogram2.services;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public interface ImageService {

    void setAvatar(@NotNull Long userId, @NotNull Long imageId);

    /**
     * @param userId        id of the user
     * @param multipartFile received image file
     * @return id of the saved file
     */
    Long save(@NotNull Long userId, @NotNull MultipartFile multipartFile);

    MultipartFile loadAvatar(@NotNull Long userId);

    MultipartFile load(@NotNull Long userId, @NotNull Long imageId);

    void delete(@NotNull Long userId, @NotNull Long imageId);

}
