package pl.tscript3r.photogram.user.role;


import lombok.Getter;
import pl.tscript3r.photogram.infrastructure.AbstractEntity;
import pl.tscript3r.photogram.infrastructure.mapper.DataStructure;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "roles")
@Cacheable
public class Role extends AbstractEntity implements DataStructure {

    @Column(nullable = false, unique = true)
    private String name;

    Role() {
    }

    public Role(final Long id, final String name) {
        super(id);
        this.name = name;
    }

}
