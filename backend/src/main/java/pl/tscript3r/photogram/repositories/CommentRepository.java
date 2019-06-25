package pl.tscript3r.photogram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tscript3r.photogram.domains.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
