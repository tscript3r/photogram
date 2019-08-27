package pl.tscript3r.photogram.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram.infrastructure.exception.ForbiddenPhotogramException;
import pl.tscript3r.photogram.user.role.Role;
import pl.tscript3r.photogram.user.role.RoleService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Optional;

import static pl.tscript3r.photogram.user.role.RoleService.ADMIN_ROLE;
import static pl.tscript3r.photogram.user.role.RoleService.MODERATOR_ROLE;

@Slf4j
@Service
public class AuthorizationService {

    private final Role adminRole;
    private final Role moderatorRole;
    private final UserService userService;

    @Lazy
    public AuthorizationService(UserService userService, RoleService roleService) {
        adminRole = roleService.getByName(ADMIN_ROLE);
        moderatorRole = roleService.getByName(MODERATOR_ROLE);
        this.userService = userService;
    }

    public Boolean isAdmin(@NotNull final User user) {
        return hasRole(user, adminRole);
    }

    public Boolean isModerator(@NotNull final User user) {
        return hasRole(user, moderatorRole);
    }

    private Boolean hasRole(final User user, final Role role) {
        return user.getRoles().contains(role);
    }

    public Optional<User> accessValidation(final Principal principal, final Long resourcesUserId) {
        // Assumption: admin & moderator roles should be able to do any CRUD operations for any user
        // in case of spring security allowing to get some resource without login then it should be allowed,
        // in case when resourcesUserId is not specified it means that user is doing something with his own
        // resource, which of course should be allowed.
        if (resourcesUserId == null || principal == null)
            return Optional.empty();
        User user = userService.getByPrincipal(principal);
        if (!(isResourceOwner(user, resourcesUserId) || isAdmin(user) || isModerator(user)))
            throwForbiddenException();
        return Optional.of(user);
    }

    private boolean isResourceOwner(final User user, final long resourcesUserId) {
        return user.getId() == resourcesUserId;
    }

    private void throwForbiddenException() {
        throw new ForbiddenPhotogramException("Forbidden");

    }

    public AuthorizationService requireLogin(Principal principal) {
        if (principal == null)
            throwForbiddenException();
        return this;
    }

}
