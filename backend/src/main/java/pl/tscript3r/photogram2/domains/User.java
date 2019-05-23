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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_likes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    private Set<Post> likedPost = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "users_dislikes",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"))
    private Set<Post> dislikedPost = new HashSet<>();

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
                String bio, Set<Role> roles, List<Post> posts, LocalDateTime creationDate) {
        super(id);
        this.firstname = firstname;
        this.username = username;
        this.password = password;
        this.email = email;
        this.emailConfirmed = emailConfirmed;
        this.bio = bio;
        this.roles = roles;
        this.posts = posts;
        this.creationDate = creationDate;
    }

    public void addRole(final Role role) {
        roles.add(role);
    }

    public Boolean addLikedPost(final Post post) {
        return likedPost.add(post);
    }

    public Boolean removeLikedPost(final Post post) {
        return likedPost.remove(post);
    }

    public Boolean addDislikedPost(final Post post) {
        return dislikedPost.add(post);
    }

    public Boolean removeDislikedPost(final Post post) {
        return dislikedPost.remove(post);
    }

    public Boolean hasLikedPost(final Post post) {
        return likedPost.contains(post);
    }

    public Boolean hasDislikedPost(final Post post) {
        return dislikedPost.contains(post);
    }

}
