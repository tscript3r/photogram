package pl.tscript3r.photogram2.domains;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "roles")
public class Role extends DomainEntity {

    @Column(nullable = false, unique = true)
    private String name;

    Role() {
    }

    public Role(Long id, String name) {
        super(id);
        this.name = name;
    }

}
