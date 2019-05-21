package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.Assert.notNull;

@Getter
@Entity
@Table(name = "posts")
public class Post extends DomainEntity {

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Column(columnDefinition = "text")
    private String caption;

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
     * <p>
     * TODO: add some thread / schedule to auto remove expired non valid posts
     */
    @Setter
    private Boolean valid = false;

    @Setter
    @Column(nullable = false)
    private Integer likes = 0;

    @Setter
    @CreationTimestamp
    private LocalDateTime creationDate;

    Post() {
    }

    public Post(final User user, final String caption, final String location) {
        final String FIELD_NEEDS_TO_BE_SET = "%s needs to be set";
        notNull(user, String.format(FIELD_NEEDS_TO_BE_SET, "user"));

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

}
