package pl.tscript3r.photogram2.services.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.domains.Role;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.ForbiddenPhotogramException;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.RoleRepository;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Lazy
    public RoleServiceImpl(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefault() {
        return getByName("USER");
    }

    @Override
    public Role getByName(@NotNull final String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundPhotogramException(String.format("Role name=%s not found", name)));
    }

    @Override
    public Boolean isAdmin(@NotNull final User user) {
        return user.getRoles().contains(getByName("ADMIN"));
    }

    @Override
    public Boolean isModerator(@NotNull final User user) {
        return user.getRoles().contains(getByName("MODERATOR"));
    }

    @Override
    public void accessValidation(final Principal principal, final Long resourcesUserId) {
        // Assumption: admin & moderator roles should be able to do any CRUD operations for any user
        // in case of spring security allowing to get some resource without login then it should be allowed
        // in case when userId is not specified it means that user is doing something with his own resource,
        // which of course should be allowed.
        if (resourcesUserId == null || principal == null)
            return;
        User user = userService.getByPrincipal(principal);
        if (!(isResourceOwner(user, resourcesUserId) || isAdmin(user) || isModerator(user)))
            throwForbiddenException();
    }

    private boolean isResourceOwner(final User user, final long resourcesUserId) {
        return user.getId() == resourcesUserId;
    }

    private void throwForbiddenException() {
        throw new ForbiddenPhotogramException("Forbidden");
    }

    @Override
    public RoleService requireLogin(Principal principal) {
        if (principal == null)
            throwForbiddenException();
        return this;
    }
}