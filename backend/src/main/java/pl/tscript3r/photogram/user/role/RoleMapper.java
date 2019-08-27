package pl.tscript3r.photogram.user.role;

import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.infrastructure.mapper.AbstractMapper;
import pl.tscript3r.photogram.infrastructure.mapper.CollectionMapper;
import pl.tscript3r.photogram.user.api.v1.RoleDto;

@Component
public class RoleMapper extends AbstractMapper<Role, RoleDto> implements CollectionMapper {

    @Override
    public RoleDto firstToSecond(final Role source) {
        return new RoleDto(source.getName());
    }

    @Override
    public Role secondToFirst(final RoleDto source) {
        return new Role(null, source.getName());
    }

}
