package pl.tscript3r.photogram2.domains;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class Post extends DomainEntity {

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(columnDefinition = "text", nullable = false)
    private String caption;

    private String location;

    @Column(nullable = false)
    private Long imageId;

    @Column(nullable = false)
    private Integer likes = 0;

    @CreationTimestamp
    private LocalDateTime creationDate;

}
