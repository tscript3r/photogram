package pl.tscript3r.photogram2.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.domains.RoleTest.*;

@DisplayName("User")
public class UserTest {

    public static User getDefaultUser() {
        var result = new User(ID, NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO,
                new HashSet<>(), null, CREATION_DATE);
        result.addRole(getAdminRole());
        result.addRole(getDefaultRole());
        result.addRole(getModeratorRole());
        return result;
    }

    public static User getSecondUser() {
        var result = new User(SECOND_ID, SECOND_NAME, SECOND_USERNAME, SECOND_PASSWORD, SECOND_EMAIL, SECOND_EMAIL_CONFIRMED,
                SECOND_BIO, new HashSet<>(), null, SECOND_CREATION_DATE);
        result.addRole(getDefaultRole());
        return result;
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
    @DisplayName("New user validation")
    void newUser() {
        var user = new User(null, NAME, USERNAME, PASSWORD, EMAIL, EMAIL_CONFIRMED, BIO,
                null, null, CREATION_DATE);
        assertTrue(user.isNew());
        user.setId(ID);
        assertFalse(user.isNew());
    }

}