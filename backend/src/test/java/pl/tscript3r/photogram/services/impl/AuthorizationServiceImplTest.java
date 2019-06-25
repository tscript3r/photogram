package pl.tscript3r.photogram.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram.exceptions.ForbiddenPhotogramException;
import pl.tscript3r.photogram.services.RoleService;
import pl.tscript3r.photogram.services.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.domains.RoleTest.*;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram.domains.UserTest.getSecondUser;

@DisplayName("Authorization service")
@ExtendWith(MockitoExtension.class)
class AuthorizationServiceImplTest {

    @Mock
    RoleService roleService;

    @Mock
    UserService userService;

    private AuthorizationServiceImpl authorizationService;

    @BeforeEach
    void init() {
        doReturn(getModeratorRole()).when(roleService).getByName(MODERATOR_ROLE_NAME);
        doReturn(getAdminRole()).when(roleService).getByName(ADMIN_ROLE_NAME);
        authorizationService = new AuthorizationServiceImpl(userService, roleService);
    }

    @Test
    @DisplayName("Is admin")
    void isAdmin() {
        var user = getDefaultUser();
        assertTrue(authorizationService.isAdmin(user));
    }

    @Test
    @DisplayName("Is not admin")
    void isNotAdmin() {
        var user = getSecondUser();
        assertFalse(authorizationService.isAdmin(user));
    }

    @Test
    @DisplayName("Is moderator")
    void isModerator() {
        var user = getDefaultUser();
        assertTrue(authorizationService.isModerator(user));
    }

    @Test
    @DisplayName("Is not moderator")
    void isNotModerator() {
        var user = getSecondUser();
        assertFalse(authorizationService.isModerator(user));
    }

    @Test
    @DisplayName("Access validation to users own resource")
    void accessValidationUsersOwnResource() {
        var user = getSecondUser();
        when(userService.getByPrincipal(any())).thenReturn(user);
        authorizationService.accessValidation(() -> SECOND_USERNAME, SECOND_ID);
        verify(userService, times(1)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Access validation without principal")
        // which means without authorization
    void accessValidationWithoutPrincipal() {
        authorizationService.accessValidation(null, SECOND_ID);
        verify(userService, times(0)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Access validation without resource user id")
        // by assumption - own resource
    void accessValidationWithoutResourcesUserId() {
        authorizationService.accessValidation(() -> SECOND_USERNAME, null);
        verify(userService, times(0)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Access validation with moderator rights to someones resource")
    void accessValidationWithModeratorRights() {
        var user = getDefaultUser();
        when(userService.getByPrincipal(any())).thenReturn(user);
        authorizationService.accessValidation(() -> USERNAME, SECOND_ID);
        verify(userService, times(1)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Access validation with admin rights to someone's resource")
    void accessValidationWithAdminRights() {
        var user = getDefaultUser();
        when(userService.getByPrincipal(any())).thenReturn(user);
        authorizationService.accessValidation(() -> USERNAME, SECOND_ID);
        verify(userService, times(1)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Access validation without privileged rights to someone's resource")
    void accessValidationWithoutPrivilegedRights() {
        var user = getSecondUser();
        when(userService.getByPrincipal(any())).thenReturn(user);
        assertThrows(ForbiddenPhotogramException.class, () -> authorizationService.accessValidation(() -> USERNAME, ID));
        verify(userService, times(1)).getByPrincipal(any());
    }

    @Test
    @DisplayName("Login required")
    void requireLogin() {
        // Nothing more - controller pass null when there is no authentication,
        // and there should be thrown Forbidden exception, and it should return
        // own instance to be able easily call access validation method after this
        assertNotNull(authorizationService.requireLogin(() -> USERNAME));
    }

    @Test
    @DisplayName("Without required login")
    void withoutRequiredLogin() {
        // Nothing more - controller pass null when there is no authentication
        assertThrows(ForbiddenPhotogramException.class, () -> authorizationService.requireLogin(null));
    }

}