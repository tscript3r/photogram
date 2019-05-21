package pl.tscript3r.photogram2.services.impl;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram2.services.ImageService;

import javax.validation.constraints.NotNull;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public Long reserveNextImageId(@NotNull Long userId) {
        return RandomUtils.nextLong();
    }

    @Override
    public void setAvatar(@NotNull final Long userId, @NotNull final Long imageId) {

    }

    @Override
    public Long save(@NotNull final Long userId, @NotNull final Long postId, @NotNull final MultipartFile multipartFile) {
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
