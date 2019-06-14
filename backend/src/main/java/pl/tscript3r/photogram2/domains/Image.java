package pl.tscript3r.photogram2.domains;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Image extends DomainEntity {

    @Getter
    private Long imageId;

    @Getter
    private String extension;

    Image() {
    }

    public Image(Long imageId, String extension) {
        this.imageId = imageId;
        this.extension = extension;
    }

    public String getFileName() {
        return imageId.toString();
    }

}
