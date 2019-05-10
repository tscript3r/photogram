package pl.tscript3r.photogram2.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.photogram2.Consts.*;

@DisplayName("User")
public class UserTest {

    public static User getDefaultUser() {
        return new User(ID, NAME, USERNAME, PASSWORD, EMAIL, BIO);
    }

    public static User getSecondUser() {
        return new User(SECOND_ID, SECOND_NAME, SECOND_USERNAME, SECOND_PASSWORD, SECOND_EMAIL,
                SECOND_BIO);
    }

    @DisplayName("User equals with ID")
    @Test
    void userEqualsWithId() {
        var secondUser = getSecondUser();
        secondUser.setId(ID);
        assertEquals(getDefaultUser(), secondUser);
    }

    @DisplayName("User not equals with ID")
    @Test
    void userNotEqualsWithId() {
        var defaultUser = getDefaultUser();
        defaultUser.setId(SECOND_ID);
        assertNotEquals(getDefaultUser(), defaultUser);
    }

    @DisplayName("New user function validation")
    @Test
    void newUser() {
        var user = new User(NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, LOCAL_DATE_TIME, Collections.emptySet());
        assertTrue(user.isNew());
        user.setId(ID);
        assertFalse(user.isNew());
    }

}