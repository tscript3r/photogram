package pl.tscript3r.photogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram.domains.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
