package pl.tscript3r.photogram.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram.domains.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Integer countByFollowed(User user);

    Integer countByFollows(User user);

    Slice<User> findByFollowed(User user, Pageable pageable);

    Slice<User> findByFollows(User user, Pageable pageable);

}
