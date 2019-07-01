package pl.tscript3r.photogram.services.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram.domains.Role;
import pl.tscript3r.photogram.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram.repositories.RoleRepository;
import pl.tscript3r.photogram.services.RoleService;
import pl.tscript3r.photogram.services.UserService;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Transactional
public class RoleServiceBean implements RoleService {

    public static final String DEFAULT_USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String MODERATOR_ROLE = "MODERATOR";

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Lazy
    public RoleServiceBean(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefault() {
        // TODO: refactor to load it once
        return getByName(DEFAULT_USER_ROLE);
    }

    @Override
    public Role getByName(@NotNull final String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundPhotogramException(String.format("Role name=%s not found", name)));
    }

}