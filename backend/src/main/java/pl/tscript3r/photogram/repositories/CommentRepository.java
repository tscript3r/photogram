package pl.tscript3r.photogram.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.tscript3r.photogram.domains.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    Slice<Comment> findByPostId(Long postId, Pageable pageable);

}
