package pl.tscript3r.photogram.api.v1.dtos;

import pl.tscript3r.photogram.post.Visibility;
import pl.tscript3r.photogram.post.api.v1.PostDto;

import static pl.tscript3r.photogram.Consts.*;

public class PostDtoTest {

    public static PostDto getDefaultPostDto() {
        var result = new PostDto();
        result.setId(ID);
        result.setUserId(ID);
        result.setCaption(CAPTION);
        result.setLocation(LOCATION);
        result.setVisibility(Visibility.PUBLIC);
        result.getImages().add(IMAGE_DTO);
        result.setLikesCount(LIKES);
        result.setCreationDate(CREATION_DATE);
        return result;
    }

    public static PostDto getSecondPostDto() {
        var result = new PostDto();
        result.setId(SECOND_ID);
        result.setUserId(SECOND_ID);
        result.setCaption(SECOND_CAPTION);
        result.setLocation(SECOND_LOCATION);
        result.setVisibility(Visibility.PUBLIC);
        result.getImages().add(SECOND_IMAGE_DTO);
        result.setLikesCount(SECOND_LIKES);
        result.setCreationDate(SECOND_CREATION_DATE);
        return result;
    }

}