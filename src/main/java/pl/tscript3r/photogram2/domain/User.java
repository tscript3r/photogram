package pl.tscript3r.photogram2.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class User extends DomainEntity {

    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private Boolean emailConfirmed = false;

    @Column(columnDefinition = "text")
    private String bio;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    public User(Long id, String name, String username, String password, String email, Boolean emailConfirmed,
                String bio, LocalDateTime creationDate, Set<Role> roles) {
        super(id);
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.bio = bio;
        this.creationDate = creationDate;
        this.roles = roles;
    }

}
