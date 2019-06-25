package pl.tscript3r.photogram.api.v1.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.api.v1.dtos.RoleDto;
import pl.tscript3r.photogram.api.v1.dtos.UserDto;
import pl.tscript3r.photogram.domains.User;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class UserMapper extends AbstractMapper<User, UserDto> implements CollectionMapper {

    private final RoleMapper roleMapper;

    @Override
    protected UserDto firstToSecond(final User source) {
        return new UserDto(source.getId(), source.getFirstname(), source.getUsername(), source.getPassword(), source.getEmail(),
                source.isEmailConfirmed(), source.getBio(), source.getCreationDate(),
                new HashSet<>(roleMapper.map(source.getRoles(), RoleDto.class)));
    }

    @Override
    protected User secondToFirst(final UserDto source) {
        return new User(source.getFirstname(), source.getUsername(), source.getPassword(), source.getEmail(), source.getBio());
    }

}
