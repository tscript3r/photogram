package pl.tscript3r.photogram.post;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.lang.Nullable;
import pl.tscript3r.photogram.infrastructure.AbstractEntity;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.post.comment.Comment;
import pl.tscript3r.photogram.post.image.Image;
import pl.tscript3r.photogram.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "posts")
@Cacheable
public class Post extends AbstractEntity {

    private static final int MAX_IMAGES_PER_POST = 10;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "post_images",
            joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "image_id", referencedColumnName = "id"))
    private List<Image> images = new ArrayList<>();

    /**
     * Until there no images are uploaded post should not be valid. When the post is
     * not valid it wont be possible to get it to the view or even to delete it, or edit.
     * Predicting that in the future there will be more requirements to have an valid post,
     * that is why this field has been added.
     */
    @Setter
    private Boolean valid = false;

    /**
     * By creating the post should be private until the upcoming images are not yet
     * uploaded. Also it gives for the user ability to set his post an visibility
     * scope
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility = Visibility.PRIVATE;

    @Column(nullable = false)
    private Integer likes = 0;

    @Column(nullable = false)
    private Integer dislikes = 0;

    @CreationTimestamp
    private LocalDateTime creationDate;

    Post() {
    }

    public Post(final User user, final String caption, final String location) {
        this.user = user;
        this.caption = caption;
        this.location = location;
    }

    public void addImage(@NotNull final Image image) {
        images.add(image);
    }

    public Image getImage(@NotNull final Long imageId) {
        for (var image : images)
            if (image.getImageId().equals(imageId))
                return image;
        throw new NotFoundPhotogramException(String.format("Image id=%d has been not found in post id=%d", imageId,
                getId()));
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
