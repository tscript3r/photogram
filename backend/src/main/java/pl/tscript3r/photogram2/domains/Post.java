package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(columnDefinition = "text", nullable = false)
    private String caption;

    private String location;

    @Setter
    @Column(nullable = false)
    private Long imageId;

    @Setter
    @Column(nullable = false)
    private Integer likes = 0;

    @Setter
    @CreationTimestamp
    private LocalDateTime creationDate;

    Post() {
    }

    public Post(User user, String caption, String location) {
        final String FIELD_NEEDS_TO_BE_SET = "%s needs to be set";
        notNull(user, String.format(FIELD_NEEDS_TO_BE_SET, "user"));

        this.user = user;
        this.caption = caption;
        this.location = location;
    }

}
