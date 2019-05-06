package pl.tscript3r.photogram2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram2.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
