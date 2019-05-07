package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domains.Role;

public interface RoleService {

    Role getDefault();

    Role getByName(String name);

}