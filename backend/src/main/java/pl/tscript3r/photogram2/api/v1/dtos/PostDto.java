package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDto implements Dto {

    private Long id;

    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CommentDto> comments = new ArrayList<>();

    @Nullable
    private String caption;

    @Nullable
    private String location;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageDto> images = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    /**
     * On creating a post user needs to declare how many pictures he want to upload to it,
     * because the post should not be visible to anyone until it is done.
     * TODO: dunno how many max it will be finally for now
     */
    @Min(1)
    @Max(5)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer imagesCount;

}
