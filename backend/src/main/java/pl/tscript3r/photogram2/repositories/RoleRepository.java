package pl.tscript3r.photogram2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram2.domains.Role;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(@NotNull String name);

}
