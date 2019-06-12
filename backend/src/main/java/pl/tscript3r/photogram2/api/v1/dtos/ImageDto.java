package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class ImageDto implements Dto {

    @Getter
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long imageId;

    public ImageDto(Long imageId) {
        this.imageId = imageId;
    }

}
