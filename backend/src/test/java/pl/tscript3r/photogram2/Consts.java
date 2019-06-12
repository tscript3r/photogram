package pl.tscript3r.photogram2;

import pl.tscript3r.photogram2.api.v1.dtos.ImageDto;
import pl.tscript3r.photogram2.domains.Image;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Consts {

    public static final Long ID = 1L;
    public static final String USERNAME = "username";
    public static final String NAME = "name";
    public static final String CAPTION = "caption";
    public static final String LOCATION = "location";
    public static final String CONTENT = "content";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email@email.com";
    public static final Boolean EMAIL_CONFIRMED = true;
    public static final String BIO = "bio";
    public static final LocalDateTime CREATION_DATE = LocalDateTime.now();
    public static final int LIKES = 1;
    public static final int DISLIKES = 1;
    public static final int IMAGES_COUNT = 1;
    public static final Long IMAGE_ID = 1L;
    public static final Image IMAGE = new Image(IMAGE_ID);
    public static final ImageDto IMAGE_DTO = new ImageDto(IMAGE_ID);

    public static final long SECOND_ID = 2L;
    public static final String SECOND_USERNAME = "second_username";
    public static final String SECOND_CONTENT = "second_content";
    public static final String SECOND_NAME = "second_name";
    public static final String SECOND_PASSWORD = "second_password";
    public static final String SECOND_EMAIL = "second_email";
    public static final Boolean SECOND_EMAIL_CONFIRMED = false;
    public static final String SECOND_BIO = "second_bio";
    public static final LocalDateTime SECOND_CREATION_DATE = LocalDateTime.now(ZoneId.of("Brazil/East"));
    public static final String SECOND_ROLE = "SECOND_ROLE";
    public static final String SECOND_CAPTION = "second_caption";
    public static final String SECOND_LOCATION = "second_location";
    public static final Long SECOND_IMAGE_ID = 2L;
    public static final Image SECOND_IMAGE = new Image(SECOND_IMAGE_ID);
    public static final ImageDto SECOND_IMAGE_DTO = new ImageDto(SECOND_IMAGE_ID);
    public static final Integer SECOND_LIKES = 2;

    public static final Long THIRD_ID = 3L;

    private Consts() {
    }

}
