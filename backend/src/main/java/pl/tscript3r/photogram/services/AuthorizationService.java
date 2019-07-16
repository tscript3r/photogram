package pl.tscript3r.photogram.services;

import org.springframework.lang.Nullable;
import pl.tscript3r.photogram.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;

public interface AuthorizationService {

    Boolean isAdmin(@NotNull final User user);

    Boolean isModerator(@NotNull final User user);

    Optional<User> accessValidation(@Nullable final Principal principal, @Nullable final Long resourcesUserId);

    AuthorizationService requireLogin(@Nullable final Principal principal);

}
