package pl.tscript3r.photogram.services.beans;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.tscript3r.photogram.infrastructure.exception.ForbiddenPhotogramException;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.user.UserDetailsServiceImpl;
import pl.tscript3r.photogram.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram.Consts.USERNAME;
import static pl.tscript3r.photogram.domains.RoleTest.getAdminRole;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;

@DisplayName("User details service")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    @DisplayName("Load user by username")
    void loadUserByUsername() {
        var user = getDefaultUser();
        when(userService.getByUsername(any())).thenReturn(user);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(userDetails.getAuthorities().iterator().next().getAuthority(), getAdminRole().getName());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Load non existing user by username")
    void loadNonExistingUserByUsername() {
        when(userService.getByUsername(any())).thenThrow(NotFoundPhotogramException.class);
        assertThrows(ForbiddenPhotogramException.class, () -> userDetailsServiceImpl.loadUserByUsername(USERNAME));
    }

}