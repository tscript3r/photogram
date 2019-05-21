package pl.tscript3r.photogram2.domains;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
public class User extends DomainEntity {

    @Setter
    @Column(nullable = false)
    private String firstname;

    @Setter
    @Column(nullable = false, unique = true)
    private String username;

    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(unique = true, nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private Boolean emailConfirmed = false;

    @Setter
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

    User() {
    }

    public User(final String firstname, final String username, final String password,
                final String email, final String bio) {
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.bio = bio;
    }

    public User(Long id, String firstname, String username, String password, String email, Boolean emailConfirmed,
                String bio, Set<Role> roles, List<Post> posts, List<Post> likedPost, LocalDateTime creationDate) {
        super(id);
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.bio = bio;
        this.roles = roles;
        this.posts = posts;
        this.likedPost = likedPost;
        this.creationDate = creationDate;
    }

    public void addRole(final Role role) {
        roles.add(role);
    }

    public void addPost(final Post post) {
        posts.add(post);
    }

    public void addLikedPost(final Post post) {
        posts.add(post);
    }

    public void removeRole(final Role role) {
        roles.remove(role);
    }

    public void removePost(final Post post) {
        posts.remove(post);
    }

    public void removeLikedPost(final Post post) {
        posts.remove(post);
    }

}
