package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;
import pl.tscript3r.photogram2.exceptions.IgnoredPhotogramException;

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

    @Nullable
    private String location;

    private Integer imagesCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "post_images",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    private List<Image> images = new ArrayList<>();

    /**
     * Until the declared images count (on creation) has not been uploaded to the post
     * post should not be valid. Until the post is not valid it wont be possible to
     * get it to the view or even to delete it, or edit. Predicting that in the future
     * there will be more requirements to have an valid post, that is why this field
     * has been added.
     *
     * TODO: add some thread / schedule to auto remove expired non valid posts
     */
    private Boolean valid = false;

    @Column(nullable = false)
    private Integer likes = 0;

    @Column(nullable = false)
    private Integer dislikes = 0;

    @CreationTimestamp
    private LocalDateTime creationDate;

    Post() {
    }

    public Post(final User user, final String caption, final String location, final Integer imagesCount) {
        this.user = user;
        this.caption = caption;
        this.location = location;
        this.imagesCount = imagesCount;
    }

    public void addImageId(@Nullable final Long id) {
        if (!isValid() && id != null && id >= 0 && images.size() < imagesCount) {
            images.add(new Image(id));
            if (isValid())
                valid = true;
        } else
            checkAddedImagesCount();
    }

    private Boolean isValid() {
        return images.size() == imagesCount;
    }

    private void checkAddedImagesCount() {
        if (images.size() >= imagesCount)
            throw new IgnoredPhotogramException(
                    String.format("Declared images count to post id=%s count is already uploaded", getId()));
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
