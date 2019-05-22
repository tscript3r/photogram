package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto implements Dto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long postId;
    private String content;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

}
