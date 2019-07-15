package pl.tscript3r.photogram.api.v1.dtos;

import java.time.LocalDateTime;

import static pl.tscript3r.photogram.Consts.*;

public class CommentDtoTest {

    public static CommentDto getDefaultCommentDto() {
        return new CommentDto(ID, ID, USERNAME, ID, CONTENT, LocalDateTime.now());
    }

    public static CommentDto getSecondCommentDto() {
        return new CommentDto(SECOND_ID, SECOND_ID, USERNAME, SECOND_ID, CONTENT, LocalDateTime.now());
    }

}