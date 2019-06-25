package pl.tscript3r.photogram.api.v1.mappers;

import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.api.v1.dtos.RoleDto;
import pl.tscript3r.photogram.domains.Role;

@Component
public class RoleMapper extends AbstractMapper<Role, RoleDto> implements CollectionMapper {

    @Override
    protected RoleDto firstToSecond(final Role source) {
        return new RoleDto(source.getName());
    }

    @Override
    protected Role secondToFirst(final RoleDto source) {
        return new Role(null, source.getName());
    }

}
