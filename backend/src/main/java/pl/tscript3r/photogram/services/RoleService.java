package pl.tscript3r.photogram.services;

import pl.tscript3r.photogram.domains.Role;

import javax.validation.constraints.NotNull;

public interface RoleService {

    Role getDefault();

    Role getByName(@NotNull final String name);

}