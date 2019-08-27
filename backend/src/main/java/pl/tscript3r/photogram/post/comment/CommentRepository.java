package pl.tscript3r.photogram.post.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    Slice<Comment> findByPostId(Long postId, Pageable pageable);

}
