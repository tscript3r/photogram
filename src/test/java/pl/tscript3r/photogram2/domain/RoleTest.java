package pl.tscript3r.photogram2.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static pl.tscript3r.photogram2.Consts.*;

class RoleTest {

    static Role getDefaultRole() {
        return new Role(ID, NAME);
    }

    static Role getSecondRole() {
        return new Role(SECOND_ID, SECOND_NAME);
    }

    @DisplayName("Role equals with ID")
    @Test
    void userEqualsWithId() {
        Role secondRole = getSecondRole();
        secondRole.setId(ID);
        assertEquals(getDefaultRole(), secondRole);
    }

    @DisplayName("Role not equals with ID")
    @Test
    void userNotEqualsWithId() {
        Role defaultRole = getDefaultRole();
        defaultRole.setId(SECOND_ID);
        assertNotEquals(getDefaultRole(), defaultRole);
    }

}