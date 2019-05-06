package pl.tscript3r.photogram2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram2.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
