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

    @Setter
    @OneToOne(fetch = FetchType.EAGER)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Setter
    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime creationDate;

    Comment() {
    }

    public Comment(final String content) {
        this.content = content;
    }

}
