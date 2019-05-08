package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;

import java.security.Principal;
import java.util.Set;

public interface UserService {

    User save(User user);

    UserDto save(UserDto user);

    UserDto update(Principal principal, UserDto userDto);

    User update(User user);

    void delete(Long id);

    Set<UserDto> getAllDto();

    User getByPrincipal(Principal principal);

    User getById(Long id);

    UserDto getByIdDto(Long id);

    User getByUsername(String username);

    UserDto getByUsernameDto(String username);

    User getByEmail(String email);

    UserDto getByEmailDto(String email);

    void resetPassword(String email);

}
