package pl.tscript3r.photogram2.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.photogram2.Consts.*;

public class UserTest {

    public static User getDefaultUser() {
        return new User(ID, NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, LOCAL_DATE_TIME, Collections.emptySet());
    }

    public static User getSecondUser() {
        return new User(SECOND_ID, SECOND_NAME, SECOND_USERNAME, SECOND_PASSWORD, SECOND_EMAIL, SECOND_EMAIL_CONFIRMED,
                SECOND_BIO, SECOND_LOCAL_DATE_TIME, Collections.emptySet());
    }

    @DisplayName("User equals with ID")
    @Test
    void userEqualsWithId() {
        User secondUser = getSecondUser();
        secondUser.setId(ID);
        assertEquals(getDefaultUser(), secondUser);
    }

    @DisplayName("User not equals with ID")
    @Test
    void userNotEqualsWithId() {
        User defaultUser = getDefaultUser();
        defaultUser.setId(SECOND_ID);
        assertNotEquals(getDefaultUser(), defaultUser);
    }

    @DisplayName("New user function validation")
    @Test
    void newUser() {
        User user = new User(NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, LOCAL_DATE_TIME, Collections.emptySet());
        assertTrue(user.isNew());
        user.setId(ID);
        assertFalse(user.isNew());
    }

}