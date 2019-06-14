package pl.tscript3r.photogram2.services;

import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram2.domains.Image;

import javax.validation.constraints.NotNull;

public interface ImageService {

    void save(@NotNull final Long postId, @NotNull final Image image,
              @NotNull final MultipartFile multipartFile);

    Long getNextId(@NotNull final Long postId);

    Boolean exists(@NotNull final Long postId, @NotNull final Image image);

}
