package pl.tscript3r.photogram.post.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import pl.tscript3r.photogram.infrastructure.mapper.Dto;
import pl.tscript3r.photogram.post.Visibility;
import pl.tscript3r.photogram.post.comment.api.v1.CommentDto;
import pl.tscript3r.photogram.post.image.api.v1.ImageDto;

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

    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<CommentDto> comments = new ArrayList<>();

    @Nullable
    private String caption;

    @Nullable
    private String location;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ImageDto> images = new ArrayList<>();


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer postCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer likesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer dislikesCount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean liked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean disliked;

    private Visibility visibility;

}
