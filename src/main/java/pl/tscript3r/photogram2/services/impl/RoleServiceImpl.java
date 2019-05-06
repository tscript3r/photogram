package pl.tscript3r.photogram2.services.impl;

import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.domain.Role;
import pl.tscript3r.photogram2.repositories.RoleRepository;
import pl.tscript3r.photogram2.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getDefault() {
        return roleRepository.findByName("USER");
    }

    @Override
    public Role getByName(String name) {
        return null;
    }

}
