package pl.tscript3r.photogram.api.v1.dtos;

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
