package pl.tscript3r.photogram.post.image.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import pl.tscript3r.photogram.infrastructure.mapper.Dto;

public class ImageDto implements Dto {

    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long imageId;

    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final String extension;

    public ImageDto(Long imageId, String extension) {
        this.imageId = imageId;
        this.extension = extension;
    }

}
