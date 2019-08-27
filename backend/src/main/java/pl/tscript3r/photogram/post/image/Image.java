package pl.tscript3r.photogram.post.image;

import lombok.Getter;
import pl.tscript3r.photogram.infrastructure.AbstractEntity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
@Cacheable
public class Image extends AbstractEntity {

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
