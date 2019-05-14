package pl.tscript3r.photogram2.domains;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Role extends DomainEntity {

    @Column(nullable = false)
    private String name;

    public Role(Long id, String name) {
        super(id);
        this.name = name;
    }

}
