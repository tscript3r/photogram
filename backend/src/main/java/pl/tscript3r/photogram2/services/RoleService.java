package pl.tscript3r.photogram2.services;

import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.domains.Role;
import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface RoleService {

    Role getDefault();

    Role getByName(@NotNull final String name);

    Boolean isAdmin(@NotNull final User user);

    Boolean isModerator(@NotNull final User user);

    void accessValidation(@Nullable final Principal principal, @Nullable final Long resourcesUserId);

    RoleService requireLogin(@Nullable final Principal principal);

}