package pl.tscript3r.photogram.services.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.tscript3r.photogram.repositories.PostRepository;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredNonValidPostRemoveSchedule {

    private static final int EXECUTION_RATE = 3600_000 * 6; // 1h * 6 = 6h

    private final PostRepository postRepository;

    @Scheduled(fixedRate = EXECUTION_RATE)
    public void removeNonValidPosts() {
        var expiredLocalDateTime = LocalDateTime.now().minusDays(1);
        var expiredPostsCount = postRepository.countByCreationDateBefore(expiredLocalDateTime);
        if (expiredPostsCount > 0) {
            postRepository.deleteByCreationDateBefore(expiredLocalDateTime);
            log.info("Removed {} non valid, expired posts", expiredPostsCount);
        }
    }

}
