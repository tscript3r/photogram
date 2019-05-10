package pl.tscript3r.photogram2.api.v1.mappers;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.mappers.MapperPhotogramException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getDefaultUserDto;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;
import static pl.tscript3r.photogram2.domains.UserTest.getSecondUser;

@DisplayName("User mapper")
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
        var user = getDefaultUser();
        when(roleMapper.map(anyCollection(), any())).thenReturn(Lists.emptyList());
        var userDto = userMapper.map(user, UserDto.class);
        compareUserWithUserDto(user, userDto);
        verify(roleMapper, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("UserDto to User map validation")
    void userDtoToUser() {
        var userDto = getDefaultUserDto();
        when(roleMapper.map(anyCollection(), any())).thenReturn(Lists.emptyList());
        var user = userMapper.map(userDto, User.class);
        compareUserWithUserDto(user, userDto);
        verify(roleMapper, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("User set to UserDto set")
    void userSetToUserDto() {
        Set<User> users = new HashSet<>();
        users.add(getDefaultUser());
        users.add(getSecondUser());
        Set<UserDto> userDtos = userMapper.map(users, UserDto.class);
        assertEquals(2, userDtos.size());
    }

    @Test
    @DisplayName("User list to UserDto list")
    void userListToUserDto() {
        List<User> users = new ArrayList<>();
        users.add(getDefaultUser());
        users.add(getSecondUser());
        List<UserDto> userDtos = userMapper.map(users, UserDto.class);
        assertEquals(2, userDtos.size());
    }

    @Test
    @DisplayName("User linkedSet to UserDto linkedSet")
    void userLinkedSetToUserDto() {
        LinkedHashSet<User> users = new LinkedHashSet<>();
        users.add(getDefaultUser());
        users.add(getSecondUser());
        LinkedHashSet<UserDto> userDtos = userMapper.map(users, UserDto.class);
        assertEquals(2, userDtos.size());
    }

    @Test
    @DisplayName("User collection mapping fail")
    void userCollectionMappingFail() {
        // Excepting that vector will be never used for true usages, because why?
        Vector<User> users = new Vector<>();
        users.add(getDefaultUser());
        assertThrows(MapperPhotogramException.class, () -> userMapper.map(users, UserDto.class));
    }

}