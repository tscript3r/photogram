package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domain.Role;

public interface RoleService {

    Role getDefault();

    Role getByName(String name);

}