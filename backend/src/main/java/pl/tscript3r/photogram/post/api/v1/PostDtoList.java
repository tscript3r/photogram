package pl.tscript3r.photogram.post.api.v1;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoList {

    private List<PostDto> content = new ArrayList<>();

}
