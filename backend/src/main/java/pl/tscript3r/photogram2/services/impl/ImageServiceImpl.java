package pl.tscript3r.photogram2.services.impl;

import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram2.services.ImageService;

import javax.validation.constraints.NotNull;

public class ImageServiceImpl implements ImageService {

    @Override
    public void setAvatar(@NotNull final Long userId, @NotNull final Long imageId) {

    }

    @Override
    public Long save(@NotNull final Long userId, @NotNull final MultipartFile multipartFile) {
        return null;
    }

    @Override
    public MultipartFile loadAvatar(@NotNull final Long userId) {
        return null;
    }

    @Override
    public MultipartFile load(@NotNull final Long userId, @NotNull final Long imageId) {
        return null;
    }

    @Override
    public void delete(@NotNull final Long userId, @NotNull final Long imageId) {

    }
}
