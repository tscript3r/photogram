package pl.tscript3r.photogram.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Integer countByFollowed(User user);

    Integer countByFollows(User user);

    Slice<User> findByFollowed(User user, Pageable pageable);

    Slice<User> findByFollows(User user, Pageable pageable);

    Slice<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);

}
