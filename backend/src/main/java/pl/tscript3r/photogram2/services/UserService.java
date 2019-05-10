package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

public interface UserService {

    User save(@NotNull User user, @NotNull Boolean passwordEncode, @NotNull Boolean addDefaultRole);

    UserDto save(@NotNull UserDto user);

    UserDto update(Principal principal, @NotNull UserDto userDto);

    void delete(@NotNull Long id);

    List<UserDto> getAllDto();

    User getByPrincipal(Principal principal);

    User getById(@NotNull Long id);

    UserDto getByIdDto(@NotNull Long id);

    User getByUsername(@NotNull String username);

    UserDto getByUsernameDto(@NotNull String username);

    User getByEmail(@NotNull String email);

    UserDto getByEmailDto(@NotNull String email);

    void resetPassword(@NotNull String email);

}
