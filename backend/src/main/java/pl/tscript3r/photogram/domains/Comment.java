package pl.tscript3r.photogram.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "comments")
public class Comment extends DomainEntity {

    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Setter
    @CreationTimestamp
    private LocalDateTime creationDate;

    public Comment() {
    }

    public Comment(final User user, final Post post, final String content) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

}
