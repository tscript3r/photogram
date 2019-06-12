package pl.tscript3r.photogram2.services;

import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;

public interface AuthorizationService {

    Boolean isAdmin(@NotNull final User user);

    Boolean isModerator(@NotNull final User user);

    void accessValidation(@Nullable final Principal principal, @Nullable final Long resourcesUserId);

    AuthorizationService requireLogin(@Nullable final Principal principal);

}
