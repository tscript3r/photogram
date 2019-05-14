package pl.tscript3r.photogram2.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.domains.Role;
import pl.tscript3r.photogram2.exceptions.services.RoleNotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.RoleRepository;
import pl.tscript3r.photogram2.services.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefault() {
        return getByName("USER");
    }

    @Override
    public Role getByName(final String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundPhotogramException(String.format("Role firstname=%s not found", name)));
    }

}
