package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

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
