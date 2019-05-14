package pl.tscript3r.photogram2.services;

import pl.tscript3r.photogram2.domains.Role;

import javax.validation.constraints.NotNull;

public interface RoleService {

    Role getDefault();

    Role getByName(@NotNull String name);

}