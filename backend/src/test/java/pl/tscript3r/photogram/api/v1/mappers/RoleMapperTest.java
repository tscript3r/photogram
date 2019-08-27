package pl.tscript3r.photogram.api.v1.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.tscript3r.photogram.user.api.v1.RoleDto;
import pl.tscript3r.photogram.user.role.Role;
import pl.tscript3r.photogram.user.role.RoleMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Role mapper")
class RoleMapperTest {

    static void compareUserRoleWithUserRoleDto(Role role, RoleDto roleDto) {
        assertEquals(role.getName(), roleDto.getName());
    }

    private RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        roleMapper = new RoleMapper();
    }

    @Test
    @DisplayName("Role to RoleDto map validation")
    void firstToSecond() {
        Role role = new Role(1L, "ADMIN");
        compareUserRoleWithUserRoleDto(role, roleMapper.firstToSecond(role));
    }

    @Test
    @DisplayName("RoleDto to Role map validation")
    void secondToFirst() {
        RoleDto roleDto = new RoleDto("ADMIN");
        compareUserRoleWithUserRoleDto(roleMapper.secondToFirst(roleDto), roleDto);
    }

}