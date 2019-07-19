package pl.tscript3r.photogram.services.beans;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tscript3r.photogram.api.v1.dtos.UserDto;
import pl.tscript3r.photogram.domains.EmailConfirmation;
import pl.tscript3r.photogram.domains.User;
import pl.tscript3r.photogram.exceptions.ForbiddenPhotogramException;
import pl.tscript3r.photogram.exceptions.IgnoredPhotogramException;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.repositories.UserRepository;
import pl.tscript3r.photogram.services.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.api.v1.dtos.UserDtoTest.getDefaultUserDto;
import static pl.tscript3r.photogram.api.v1.dtos.UserDtoTest.getSecondUserDto;
import static pl.tscript3r.photogram.api.v1.services.beans.MapperServiceBeanTest.getInstance;
import static pl.tscript3r.photogram.domains.RoleTest.getDefaultRole;
import static pl.tscript3r.photogram.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram.domains.UserTest.getSecondUser;

@DisplayName("User service")
@ExtendWith(MockitoExtension.class)
class UserServiceBeanTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    AuthorizationService authorizationService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ImageService imageService;

    @Mock
    EmailService emailService;

    @Mock
    Pageable pageable;

    private UserService userService;

    @BeforeEach
    void setUp() {
        var mapperService = getInstance();
        userService = new UserServiceBean(userRepository, roleService, authorizationService, passwordEncoder, imageService,
                emailService, mapperService);
    }

    @Test
    @DisplayName("Save domain")
    void saveDomain() {
        var user = getDefaultUser();
        when(roleService.getDefault()).thenReturn(getDefaultRole());
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
        when(userRepository.save(any())).thenReturn(user);
        userService.save(user, true, true);
        verify(roleService, times(1)).getDefault();
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Save domain without pass encode & default role")
    void saveDomainWithoutPassEncodeAndDefaultRole() {
        var user = getDefaultUser();
        when(emailService.createEmailConfirmation(any(), any()))
                .thenReturn(new EmailConfirmation(user, UUID.randomUUID(), false));
        when(userRepository.save(any())).thenReturn(user);

        userService.save(user, false, false);

        verify(roleService, times(0)).getDefault();
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(1)).save(any());
        verify(emailService, times(1)).createEmailConfirmation(any(), any());
    }

    @Test
    @DisplayName("Save DTO")
    void saveDto() {
        var userDto = getDefaultUserDto();
        when(roleService.getDefault()).thenReturn(getDefaultRole());
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
        when(userRepository.save(any())).thenReturn(getDefaultUser());
        userService.save(userDto);
        verify(roleService, times(1)).getDefault();
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Update DTO by ID")
    void updateDtoById() {
        Principal principal = () -> USERNAME;
        var userDto = getSecondUserDto();
        var modifiedUser = getDefaultUser();

        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(userRepository.findById(any())).thenReturn(Optional.of(modifiedUser));
        when(userRepository.save(any())).thenReturn(modifiedUser);
        when(passwordEncoder.encode(any())).thenReturn(SECOND_PASSWORD);

        var modifiedUserDto = userService.update(principal, ID, userDto);
        assertEquals(SECOND_EMAIL, modifiedUserDto.getEmail());
        assertEquals(SECOND_BIO, modifiedUserDto.getBio());
        assertEquals(SECOND_NAME, modifiedUserDto.getFirstname());
        assertEquals(SECOND_PASSWORD, modifiedUserDto.getPassword());
        assertEquals(SECOND_USERNAME, modifiedUserDto.getUsername());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
    }

    @Test
    @DisplayName("Update DTO without id")
    void updateDtoWithoutEmailAndId() {
        var userDto = new UserDto();
        userDto.setFirstname(SECOND_NAME);
        userDto.setUsername(SECOND_USERNAME);
        Principal principal = () -> USERNAME;

        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);

        assertThrows(NotFoundPhotogramException.class, () -> userService.update(principal, ID, userDto));
    }

    @Test
    @DisplayName("Update DTO without login")
    void updateDtoWithoutLogin() {
        when(authorizationService.requireLogin(any())).thenThrow(ForbiddenPhotogramException.class);
        assertThrows(ForbiddenPhotogramException.class, () -> userService.update(null, ID, getDefaultUserDto()));
        verify(authorizationService, times(1)).requireLogin(any());
    }

    @Test
    @DisplayName("Update DTO without resource access rights")
    void updateDotWithoutAccessRights() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).accessValidation(any(), any());
        assertThrows(ForbiddenPhotogramException.class, () -> userService.delete(() -> USERNAME, 1L));
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
    }

    @Test
    @DisplayName("Get all DTOs")
    void getAllDto() {
        var users = new ArrayList<User>();
        users.add(getDefaultUser());
        users.add(getSecondUser());
        when(userRepository.findAll()).thenReturn(users);
        var userDtos = userService.getAllDto();
        assertEquals(2, userDtos.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Get by principal")
    void getByPrincipal() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDefaultUser()));
        assertNotNull(userService.getByPrincipal(() -> USERNAME));
    }

    @Test
    @DisplayName("Get by principal with null arg")
    void getByPrincipalWithNullArg() {
        assertThrows(ForbiddenPhotogramException.class, () -> userService.getByPrincipal(null));
    }

    @Test
    @DisplayName("Get by existing ID")
    void getById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getDefaultUser()));
        assertEquals(getDefaultUser(), userService.getById(ID));
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Get by non existing ID")
    void getByNonExistingId() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.getById(any()));
    }

    @Test
    @DisplayName("Get DTO by existing ID")
    void getByIdDto() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getDefaultUser()));
        var returnedUserDto = userService.getByIdDto(any());
        assertEquals(getDefaultUser().getEmail(), returnedUserDto.getEmail());
        verify(userRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Get DTO by non existing ID")
    void getByNonExistingIdDto() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.getByIdDto(any()));
    }

    @Test
    @DisplayName("Get by username")
    void getByUsername() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDefaultUser()));
        assertEquals(getDefaultUser(), userService.getByUsername(USERNAME));
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    @DisplayName("Get by non existing username")
    void getByNonExistingUsername() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.getByUsername(USERNAME));
    }

    @Test
    @DisplayName("Get DTO by existing username")
    void getByUsernameDto() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDefaultUser()));
        var returnedUserDto = userService.getByUsernameDto(any());
        assertEquals(getDefaultUser().getEmail(), returnedUserDto.getEmail());
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    @DisplayName("Get DTO by non existing username")
    void getByNonExistingUsernameDto() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.getByUsernameDto(USERNAME));
    }

    @Test
    @DisplayName("Get by email")
    void getByEmail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(getDefaultUser()));
        assertEquals(getDefaultUser(), userService.getByEmail(EMAIL));
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    @DisplayName("Get by non existing email")
    void getByNonExistingEmail() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(getDefaultUser()));
        assertEquals(getDefaultUser(), userService.getByEmail(EMAIL));
        verify(userRepository, times(1)).findByEmail(any());
    }

    @Test
    @DisplayName("Get DTO by email")
    void getByEmailDto() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getDefaultUser()));
        UserDto returnedUserDto = userService.getByUsernameDto(any());
        assertEquals(getDefaultUser().getEmail(), returnedUserDto.getEmail());
        verify(userRepository, times(1)).findByUsername(any());
    }

    @Test
    @DisplayName("Get DTO by non existing email")
    void getByNonExistingEmailDto() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.getByEmailDto(EMAIL));
    }

    @Test
    @DisplayName("Delete by ID")
    void delete() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(userRepository.existsById(any())).thenReturn(true);
        userService.delete(() -> USERNAME, 1L);
        verify(userRepository, times(1)).deleteById(any());
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
    }

    @Test
    @DisplayName("Delete by non existing ID")
    void deleteNonExistingId() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundPhotogramException.class, () -> userService.delete(() -> USERNAME, 1L));
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
    }

    @Test
    @DisplayName("Delete by ID other user without access rights")
    void deleteWithoutResourceAccessRights() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).accessValidation(any(), any());
        assertThrows(ForbiddenPhotogramException.class, () -> userService.delete(() -> USERNAME, 1L));
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
    }

    @Test
    @DisplayName("Delete without login")
    void deleteWithoutLogin() {
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).requireLogin(any());
        assertThrows(ForbiddenPhotogramException.class, () -> userService.delete(() -> USERNAME, 1L));
        verify(authorizationService, times(1)).requireLogin(any());
    }

    @Test
    @DisplayName("Get avatar")
    void getAvatar() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(getDefaultUser()));
        when(imageService.getAvatar(any())).thenReturn(IMAGE_RESPONSE_ENTITY);

        assertNotNull(userService.getAvatar(ID));

        verify(userRepository, times(1)).findById(any());
        verify(imageService, times(1)).getAvatar(any());
    }

    @Test
    @DisplayName("Save avatar")
    void saveAvatar() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(getDefaultUser()));

        userService.saveAvatar(() -> USERNAME, ID, IMAGE_MOCK_MULTIPART_FILE);

        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
        verify(userRepository, times(1)).findById(any());
        verify(imageService, times(1)).saveAvatar(any(), any());
    }

    @Test
    @DisplayName("Confirm email")
    void confirmEmail() {
        userService.confirmEmail(RandomStringUtils.randomNumeric(32));
        verify(emailService, times(1)).setEmailConfirmed(any());
    }

    @Test
    @DisplayName("Reset password")
    void resetPassword() {
        var user = getDefaultUser();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(any())).thenReturn(SECOND_PASSWORD);

        userService.resetPassword(EMAIL);

        assertEquals(SECOND_PASSWORD, user.getPassword());
        verify(userRepository, times(1)).findByEmail(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
        verify(emailService, times(1)).sendNewPassword(any(), any());
    }

    @Test
    @DisplayName("Reset password (email not found)")
    void resetPasswordEmailNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> userService.resetPassword(any()));
    }

    @Test
    @DisplayName("Follow")
    void follow() {
        var user = getDefaultUser();
        var followUser = getSecondUser();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(followUser));

        userService.follow(() -> USERNAME, ID);

        verify(userRepository, times(2)).save(any());
        assertTrue(user.isFollowing(followUser));
    }

    @Test
    @DisplayName("Follow already followed")
    void followFollowed() {
        var user = getDefaultUser();
        var followUser = getSecondUser();
        user.follow(followUser);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(followUser));

        assertThrows(IgnoredPhotogramException.class, () -> userService.follow(() -> USERNAME, ID));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Unfollow")
    void unfollow() {
        var user = getDefaultUser();
        var unfollowUser = getSecondUser();
        user.follow(unfollowUser);

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(unfollowUser));

        userService.unfollow(() -> USERNAME, ID);

        verify(userRepository, times(2)).save(any());
        assertFalse(user.isFollowing(unfollowUser));
    }

    @Test
    @DisplayName("Unfollow unfollowed")
    void unfollowUnfollowed() {
        var user = getDefaultUser();
        var unfollowUser = getSecondUser();

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(userRepository.findById(any())).thenReturn(Optional.of(unfollowUser));

        assertThrows(IgnoredPhotogramException.class, () -> userService.unfollow(() -> USERNAME, ID));

        verify(userRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Get followers")
    void getFollowers() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getDefaultUser()));
        when(userRepository.findByFollowed(any(), any())).thenReturn(new SliceImpl<>(Collections.emptyList()));

        assertNotNull(userService.getFollowers(ID, pageable));
    }

    @Test
    @DisplayName("Get follows")
    void getFollows() {
        when(userRepository.findById(any())).thenReturn(Optional.of(getDefaultUser()));
        when(userRepository.findByFollows(any(), any())).thenReturn(new SliceImpl<>(Collections.emptyList()));

        assertNotNull(userService.getFollows(ID, pageable));
    }

}