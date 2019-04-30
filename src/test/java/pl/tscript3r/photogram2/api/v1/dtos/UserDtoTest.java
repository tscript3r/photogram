package pl.tscript3r.photogram2.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.photogram2.Consts.*;

public class UserDtoTest {

    public static UserDto getDefaultUserDto() {
        return new UserDto(ID, NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, LocalDateTime.now(), new HashSet<>());
    }

}