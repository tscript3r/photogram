package pl.tscript3r.photogram.user.email;

import org.springframework.data.repository.CrudRepository;
import pl.tscript3r.photogram.user.User;

import java.util.Optional;
import java.util.UUID;

public interface EmailConfirmationRepository extends CrudRepository<EmailConfirmation, Long> {

    Optional<EmailConfirmation> findByUser(User user);

    Boolean existsByToken(UUID token);

    Optional<EmailConfirmation> findByToken(UUID token);

}
