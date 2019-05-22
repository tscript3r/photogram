package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "posts")
public class Post extends DomainEntity {

    @Setter
    @OneToOne
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Setter
    @Column(columnDefinition = "text")
    private String caption;

    @Setter
    @Nullable
    private String location;

    @Nullable
    private Long imageId;

    /**
     * Until the imageId is not set the post should not be valid,
     * and what comes with it - will not be able to find, or even
     * edit. It just wont exists until the image will be uploaded.
     * This could be done just with imageId (not null), but in the
     * future I'm predicting, that there will be more requirements
     * to have an valid post, that is why I have added this field.
     *
     * TODO: add some thread / schedule to auto remove expired non valid posts
     */
    @Setter
    private Boolean valid = false;

    @Setter
    @Column(nullable = false)
    private Integer likes = 0;

    @Setter
    @Column(nullable = false)
    private Integer dislikes = 0;

    @Setter
    @CreationTimestamp
    private LocalDateTime creationDate;

    Post() {
    }

    public Post(final User user, final String caption, final String location) {
        this.user = user;
        this.caption = caption;
        this.location = location;
    }

    public void setImageId(@Nullable final Long id) {
        if (id != null && id >= 0) {
            valid = true;
            imageId = id;
        }
    }

    public void incrementLikes() {
        likes++;
    }

    public void decrementLikes() {
        if (likes > 0)
            likes--;
    }

    public void incrementDislikes() {
        dislikes++;
    }

    public void decrementDislikes() {
        if (dislikes > 0)
            dislikes--;
    }

}
