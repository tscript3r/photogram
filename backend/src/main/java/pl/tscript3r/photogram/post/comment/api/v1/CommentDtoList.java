package pl.tscript3r.photogram.post.comment.api.v1;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoList {

    private List<CommentDto> content = new ArrayList<>();

}
