package pl.tscript3r.photogram2.domains;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Comment extends DomainEntity {

    @OneToOne
    private User user;

    @ManyToOne
    private Post post;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime creationDate;

}
