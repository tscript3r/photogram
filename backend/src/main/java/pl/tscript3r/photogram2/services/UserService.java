package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

public interface UserService {

    User save(@NotNull final User user, @NotNull final Boolean passwordEncode, @NotNull final Boolean addDefaultRole);

    UserDto save(@NotNull final UserDto user);

    UserDto update(Principal principal, @NotNull final UserDto userDto);

    void delete(final @NotNull Long id);

    List<UserDto> getAllDto();

    User getByPrincipal(Principal principal);

    User getById(@NotNull final Long id);

    UserDto getByIdDto(@NotNull final Long id);

    User getByUsername(@NotNull final String username);

    UserDto getByUsernameDto(@NotNull final String username);

    User getByEmail(@NotNull final String email);

    UserDto getByEmailDto(@NotNull final String email);

    void resetPassword(@NotNull final String email);

}
