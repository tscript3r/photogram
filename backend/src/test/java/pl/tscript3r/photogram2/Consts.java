package pl.tscript3r.photogram2;

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
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.now();
    public static final int LIKES = 1;
    public static final long USER_IMAGE_ID = 2;

    public static final long SECOND_ID = 2L;
    public static final String SECOND_USERNAME = "second_username";
    public static final String SECOND_CONTENT = "second_content";
    public static final String SECOND_NAME = "second_name";
    public static final String SECOND_PASSWORD = "second_password";
    public static final String SECOND_EMAIL = "second_email";
    public static final Boolean SECOND_EMAIL_CONFIRMED = false;
    public static final String SECOND_BIO = "second_bio";
    public static final LocalDateTime SECOND_LOCAL_DATE_TIME = LocalDateTime.now(ZoneId.of("Brazil/East"));
    public static final String SECOND_ROLE = "SECOND_ROLE";

    private Consts() {
    }

}
