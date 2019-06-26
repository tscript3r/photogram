package pl.tscript3r.photogram.services.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram.repositories.PostRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpiredNonValidPostRemoveScheduleTest {

    @Mock
    PostRepository postRepository;

    @InjectMocks
    ExpiredNonValidPostRemoveSchedule expiredNonValidPostRemoveSchedule;

    @Test
    @DisplayName("Remove non valid posts (without any)")
    void removeNonValidPostsWithoutAny() {
        when(postRepository.countByCreationDateBefore(any())).thenReturn(0);
        expiredNonValidPostRemoveSchedule.removeNonValidPosts();
        verify(postRepository, times(1)).countByCreationDateBefore(any());
        verify(postRepository, times(0)).deleteByCreationDateBefore(any());
    }

    @Test
    @DisplayName("Remove non valid posts (with few)")
    void removeNonValidPostsWithFew() {
        when(postRepository.countByCreationDateBefore(any())).thenReturn(3);
        expiredNonValidPostRemoveSchedule.removeNonValidPosts();
        verify(postRepository, times(1)).countByCreationDateBefore(any());
        verify(postRepository, times(1)).deleteByCreationDateBefore(any());
    }

}