package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.RoleDto;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domain.Role;
import pl.tscript3r.photogram2.domain.User;

import java.util.HashSet;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> implements CollectionMapper {

    private final RoleMapper roleMapper;

    @Lazy
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    protected UserDto firstToSecond(User source) {
        return new UserDto(source.getId(), source.getName(), source.getUsername(), source.getPassword(), source.getEmail(),
                source.getEmailConfirmed(), source.getBio(), source.getCreationDate(),
                new HashSet<>(roleMapper.map(source.getRoles(), RoleDto.class)));
    }

    @Override
    protected User secondToFirst(UserDto source) {
        return new User(source.getName(), source.getUsername(), source.getPassword(), source.getEmail(),
                source.getEmailConfirmed(), source.getBio(), source.getCreationDate(),
                new HashSet<>(roleMapper.map(source.getRoles(), Role.class)));
    }

}
