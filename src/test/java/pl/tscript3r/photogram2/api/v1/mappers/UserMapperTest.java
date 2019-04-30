package pl.tscript3r.photogram2.api.v1.mappers;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domain.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getDefaultUserDto;
import static pl.tscript3r.photogram2.domain.UserTest.getDefaultUser;
import static pl.tscript3r.photogram2.domain.UserTest.getSecondUser;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private UserMapper userMapper;

    static void compareUserWithUserDto(User user, UserDto userDto) {
        assertEquals(user.getBio(), userDto.getBio());
        assertEquals(user.getCreationDate(), userDto.getCreationDate());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getUsername(), userDto.getUsername());
    }

    @Mock
    RoleMapper roleMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper(roleMapper);
    }

    @Test
    @DisplayName("User to UserDto map validation")
    void userToUserDto() {
        User user = getDefaultUser();
        when(roleMapper.map(anyCollection(), any())).thenReturn(Lists.emptyList());
        UserDto userDto = userMapper.map(user, UserDto.class);
        compareUserWithUserDto(user, userDto);
        verify(roleMapper, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("UserDto to User map validation")
    void userDtoToUser() {
        UserDto userDto = getDefaultUserDto();
        when(roleMapper.map(anyCollection(), any())).thenReturn(Lists.emptyList());
        User user = userMapper.map(userDto, User.class);
        compareUserWithUserDto(user, userDto);
        verify(roleMapper, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("User collection to UserDto list collection")
    void userCollectionToUserDto() {
        Set<User> users = new HashSet<>();
        users.add(getDefaultUser());
        users.add(getSecondUser());
        List<UserDto> userDtos = userMapper.map(users, UserDto.class);
        assertEquals(2, userDtos.size());
    }

}