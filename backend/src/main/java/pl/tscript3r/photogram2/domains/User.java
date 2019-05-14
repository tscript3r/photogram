package pl.tscript3r.photogram2.domains;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@Entity
public class User extends DomainEntity {

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean emailConfirmed = false;

    @Column(columnDefinition = "text")
    private String bio;

    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Post> likedPost = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime creationDate;

    public User(String firstname, String username, String password, String email, Boolean emailConfirmed,
                String bio, LocalDateTime creationDate, Set<Role> roles) {
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.emailConfirmed = emailConfirmed;
        this.email = email;
        this.bio = bio;
        this.creationDate = creationDate;
        this.roles = roles;
    }

    public User(Long id, String firstname, String username, String password, String email, String bio) {
        super(id);
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
    }

    public User(String firstname, String username, String password, String email, String bio) {
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

}
