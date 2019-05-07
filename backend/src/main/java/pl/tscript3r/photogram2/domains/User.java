package pl.tscript3r.photogram2.domains;

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

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean emailConfirmed = false;

    @Column(columnDefinition = "text")
    private String bio;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User(Long id, String name, String username, String password, String email, String bio) {
        super(id);
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
