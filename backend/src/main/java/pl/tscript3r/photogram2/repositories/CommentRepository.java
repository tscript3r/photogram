package pl.tscript3r.photogram2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram2.domains.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
