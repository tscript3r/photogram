package pl.tscript3r.photogram2.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import pl.tscript3r.photogram2.domains.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
}
