package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram2.Consts.USERNAME;
import static pl.tscript3r.photogram2.domains.RoleTest.getDefaultRole;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;

@DisplayName("User details service")
@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    @DisplayName("Load user by username")
    void loadUserByUsername() {
        var user = getDefaultUser();
        user.addRole(getDefaultRole());
        when(userService.getByUsername(any())).thenReturn(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(USERNAME);
        assertEquals(USERNAME, userDetails.getUsername());
        assertEquals(userDetails.getAuthorities().iterator().next().getAuthority(), getDefaultRole().getName());
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    @DisplayName("Load non existing user by username")
    void loadNonExistingUserByUsername() {
        when(userService.getByUsername(any())).thenThrow(NotFoundPhotogramException.class);
        assertThrows(NotFoundPhotogramException.class, () -> userDetailsService.loadUserByUsername(USERNAME));
    }

}