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

    @Test
    @DisplayName("Equals only with same ID")
    void userEqualsWithId() {
        var secondUser = getSecondUser();
        secondUser.setId(ID);
        assertEquals(getDefaultUser(), secondUser);
    }

    @Test
    @DisplayName("Not equals with only different ID")
    void userNotEqualsWithId() {
        var defaultUser = getDefaultUser();
        defaultUser.setId(SECOND_ID);
        assertNotEquals(getDefaultUser(), defaultUser);
    }

    @Test
    @DisplayName("New user function validation")
    void newUser() {
        var user = new User(NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO, CREATION_DATE, Collections.emptySet());
        assertTrue(user.isNew());
        user.setId(ID);
        assertFalse(user.isNew());
    }

}