package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.Nullable;

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
    private Long imageId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

}
