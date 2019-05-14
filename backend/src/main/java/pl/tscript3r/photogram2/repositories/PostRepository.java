package pl.tscript3r.photogram2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram2.domains.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
