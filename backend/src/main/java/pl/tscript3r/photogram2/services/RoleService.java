package pl.tscript3r.photogram2.services;

import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.domains.Role;
import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface RoleService {

    enum Operations {CREATE, READ, UPDATE, DELETE}

    ;

    Role getDefault();

    Role getByName(@NotNull final String name);

    Boolean isAdmin(User user);

    void accessValidation(@NotNull Operations operations, @Nullable Principal principal, @Nullable Long userId);

    // TODO: Boolean / void (throw) accessValidation(@NotNull CRUDOperations.CREATE, @Nullable Principal principal, @Nullable Long userId)

}