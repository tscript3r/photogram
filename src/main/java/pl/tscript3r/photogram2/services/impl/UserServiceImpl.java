package pl.tscript3r.photogram2.services.impl;

import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domain.User;
import pl.tscript3r.photogram2.services.UserService;

import java.security.Principal;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public UserDto save(UserDto user) {
        return null;
    }

    @Override
    public UserDto update(Principal principal, UserDto userDto) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Set<UserDto> getAllDto() {
        return null;
    }

    @Override
    public User getByPrincipal(Principal principal) {
        return null;
    }

    @Override
    public UserDto getByIdDto(Long id) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public UserDto getByUsernameDto(String username) {
        return null;
    }

    @Override
    public UserDto getByEmailDto(String username) {
        return null;
    }

}
