package pl.tscript3r.photogram2.api.v1.dtos;

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
