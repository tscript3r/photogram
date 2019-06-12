package pl.tscript3r.photogram2.domains;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Image extends DomainEntity {

    @Getter
    private Long imageId;

    Image() {
    }

    public Image(Long imageId) {
        this.imageId = imageId;
    }

}
