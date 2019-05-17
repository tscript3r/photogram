package pl.tscript3r.photogram2.api.v1.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram2.api.v1.dtos.RoleDto;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;

import java.util.HashSet;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> implements CollectionMapper {

    private final RoleMapper roleMapper;

    @Lazy
    public UserMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    protected UserDto firstToSecond(final User source) {
        return new UserDto(source.getId(), source.getFirstname(), source.getUsername(), source.getPassword(), source.getEmail(),
                source.getEmailConfirmed(), source.getBio(), source.getCreationDate(),
                new HashSet<>(roleMapper.map(source.getRoles(), RoleDto.class)));
    }

    @Override
    protected User secondToFirst(final UserDto source) {
        return new User(source.getName(), source.getUsername(), source.getPassword(), source.getEmail(), source.getBio());
    }

}
