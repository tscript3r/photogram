package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.UserRepository;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getDefaultUserDto;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getSecondUserDto;
import static pl.tscript3r.photogram2.api.v1.services.impl.MapperServiceImplTest.getInstance;
import static pl.tscript3r.photogram2.domains.RoleTest.getDefaultRole;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram2.domains.UserTest.getSecondUser;

@DisplayName("User service")
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleService roleService;

    @Mock
    PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        var mapperService = getInstance();
        userService = new UserServiceImpl(userRepository, roleService, passwordEncoder, mapperService);
    }

    @Test
    @DisplayName("Save domain")
    void saveDomain() {
        var user = getDefaultUser();
        when(roleService.getDefault()).thenReturn(getDefaultRole());
        when(passwordEncoder.encode(any())).thenReturn(PASSWORD);
        userService.save(user, true, true);
        verify(roleService, times(1)).getDefault();
        verify(passwordEncoder, times(1)).encode(any());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Save domain without pass encode & default role")
    void saveDomainWithoutPassEncodeAndDefaultRole() {
        var user = getDefaultUser();
        userService.save(user, false, false);
        verify(roleService, times(0)).getDefault();
        verify(passwordEncoder, times(0)).encode(any());
        verify(userRepository, times(1)).save(any());
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

        when(userRepository.findById(any())).thenReturn(Optional.of(modifiedUser));
        when(userRepository.save(any())).thenReturn(modifiedUser);
        when(passwordEncoder.encode(any())).thenReturn(SECOND_PASSWORD);

        var modifiedUserDto = userService.update(principal, userDto);
        assertEquals(SECOND_EMAIL, modifiedUserDto.getEmail());
        assertEquals(SECOND_BIO, modifiedUserDto.getBio());
        assertEquals(SECOND_NAME, modifiedUserDto.getName());
        assertEquals(SECOND_PASSWORD, modifiedUserDto.getPassword());
        assertEquals(SECOND_USERNAME, modifiedUserDto.getUsername());
        verify(userRepository, times(1)).findById(any());
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());
    }

    @Test
    @DisplayName("Update DTO by email")
    void updateDtoByEmail() {
        Principal principal = () -> USERNAME;
        var userDto = getSecondUserDto();
        var modifiedUser = getDefaultUser();
        userDto.setId(null);
        userDto.setPassword(null);
        userDto.setBio(null);
        userDto.setName(null);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(modifiedUser));
        when(userRepository.save(any())).thenReturn(modifiedUser);

        UserDto modifiedUserDto = userService.update(principal, userDto);

        assertFalse(modifiedUserDto.getEmailConfirmed());
        assertEquals(SECOND_EMAIL, modifiedUserDto.getEmail());
        assertEquals(SECOND_USERNAME, modifiedUserDto.getUsername());

        assertEquals(PASSWORD, modifiedUserDto.getPassword());
        assertEquals(NAME, modifiedUserDto.getName());
        assertEquals(BIO, modifiedUserDto.getBio());

        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).save(any());
        verify(passwordEncoder, times(0)).encode(any());
    }

    @Test
    @DisplayName("Update DTO without email & id")
    void updateDtoWithoutEmailAndId() {
        var userDto = new UserDto();
        userDto.setName(SECOND_NAME);
        userDto.setUsername(SECOND_USERNAME);
        Principal principal = () -> USERNAME;

        assertThrows(NotFoundPhotogramException.class, () -> userService.update(principal, userDto));
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
    void getByPrincipal() {
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
        when(userRepository.existsById(any())).thenReturn(true);
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("Delete by non existing ID")
    void deleteNonExistingId() {
        when(userRepository.existsById(any())).thenReturn(false);
        assertThrows(NotFoundPhotogramException.class, () -> userService.delete(1L));
    }

}