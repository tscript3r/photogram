package pl.tscript3r.photogram.api.v1.dtos;

import pl.tscript3r.photogram.user.api.v1.UserDto;

import java.time.LocalDateTime;
import java.util.HashSet;

import static pl.tscript3r.photogram.Consts.*;

public class UserDtoTest {

    public static UserDto getDefaultUserDto() {
        return new UserDto(ID, NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, LocalDateTime.now(), new HashSet<>());
    }

    public static UserDto getSecondUserDto() {
        return new UserDto(SECOND_ID, SECOND_NAME, SECOND_USERNAME, SECOND_PASSWORD, SECOND_EMAIL,
                SECOND_EMAIL_CONFIRMED, SECOND_BIO, LocalDateTime.now(), new HashSet<>());
    }

}