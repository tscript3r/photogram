package pl.tscript3r.photogram.domains;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.user.role.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static pl.tscript3r.photogram.Consts.*;

@DisplayName("Role")
public class RoleTest {

    public static final String USER_ROLE_NAME = "USER";
    public static final String ADMIN_ROLE_NAME = "ADMIN";
    public static final String MODERATOR_ROLE_NAME = "MODERATOR";

    public static Role getDefaultRole() {
        return new Role(ID, USER_ROLE_NAME);
    }

    public static Role getAdminRole() {
        return new Role(SECOND_ID, ADMIN_ROLE_NAME);
    }

    public static Role getModeratorRole() {
        return new Role(THIRD_ID, MODERATOR_ROLE_NAME);
    }

    @DisplayName("Role equals with ID")
    @Test
    void userEqualsWithId() {
        Role secondRole = new Role(ID, SECOND_NAME);
        assertEquals(getDefaultRole(), secondRole);
    }

    @DisplayName("Role not equals with ID")
    @Test
    void userNotEqualsWithId() {
        Role defaultRole = new Role(SECOND_ID, NAME);
        assertNotEquals(getDefaultRole(), defaultRole);
    }

}