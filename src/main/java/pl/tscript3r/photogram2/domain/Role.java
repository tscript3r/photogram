package pl.tscript3r.photogram2.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Role extends DomainEntity {

    private String name;

    public Role(Long id, String name) {
        super(id);
        this.name = name;
    }

}
