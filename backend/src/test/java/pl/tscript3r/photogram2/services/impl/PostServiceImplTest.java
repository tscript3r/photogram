package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.DataStructure;
import pl.tscript3r.photogram2.domains.Post;
import pl.tscript3r.photogram2.exceptions.ForbiddenPhotogramException;
import pl.tscript3r.photogram2.exceptions.IgnoredPhotogramException;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.PostRepository;
import pl.tscript3r.photogram2.services.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.api.v1.dtos.PostDtoTest.getDefaultPostDto;
import static pl.tscript3r.photogram2.domains.PostTest.getDefaultPost;
import static pl.tscript3r.photogram2.domains.UserTest.getDefaultUser;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    UserService userService;

    @Mock
    RoleService roleService;

    @Mock
    PostRepository postRepository;

    @Mock
    ImageService imageService;

    @Mock
    Pageable pageable;

    @Mock
    MapperService mapperService;

    @Mock
    AuthorizationService authorizationService;

    @InjectMocks
    PostServiceImpl postService;

    @Test
    @DisplayName("Get latest with default pageable")
    void getLatest() {
        when(postRepository.findAllByValidIsTrue(any())).thenReturn(getDomainSlice());
        when(mapperService.map(anyCollection(), any())).thenReturn(getDtoList());
        var result = postService.getLatest(pageable);
        assertEquals(1, result.getContent().size());
        verify(postRepository, times(1)).findAllByValidIsTrue(any());
        verify(mapperService, times(1)).map(anyCollection(), any());
    }

    private Slice<Post> getDomainSlice() {
        return new SliceImpl<>(Collections.singletonList(getDefaultPost()));
    }

    private List<PostDto> getDtoList() {
        return Collections.singletonList(getDefaultPostDto());
    }

    @Test
    @DisplayName("Get latest from username")
    void getLatestFromUser() {
        when(userService.getByUsername(any())).thenReturn(getDefaultUser());
        when(postRepository.findByUserIdAndValidIsTrue(any(), any())).thenReturn(getDomainSlice());
        when(mapperService.map(anyCollection(), any())).thenReturn(getDtoList());
        var result = postService.getLatest(USERNAME, pageable);
        assertEquals(1, result.getContent().size());
        verify(userService, times(1)).getByUsername(any());
        verify(postRepository, times(1)).findByUserIdAndValidIsTrue(any(), any());
        verify(mapperService, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("Get latest from principal")
    void getLatestFromPrincipal() {
        when(userService.getByPrincipal(any())).thenReturn(getDefaultUser());
        when(postRepository.findByUserIdAndValidIsTrue(any(), any())).thenReturn(getDomainSlice());
        when(mapperService.map(anyCollection(), any())).thenReturn(getDtoList());
        var result = postService.getLatest(() -> USERNAME, pageable);
        assertEquals(1, result.getContent().size());
        verify(userService, times(1)).getByPrincipal(any());
        verify(postRepository, times(1)).findByUserIdAndValidIsTrue(any(), any());
        verify(mapperService, times(1)).map(anyCollection(), any());
    }

    @Test
    @DisplayName("Get existing by id")
    void getExistingById() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        assertNotNull(postService.getById(ID));
        verify(postRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Get non existing by id")
    void getNonExistingById() {
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> postService.getById(ID));
        verify(postRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Get existing DTO by id")
    void getExistingDtoById() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());
        assertNotNull(postService.getByIdDto(ID));
        verify(postRepository, times(1)).findById(any());
        verify(mapperService, times(1)).map(any(Post.class), any());
    }

    @Test
    @DisplayName("Get non existing DTO by id")
    void getNonExistingDtoById() {
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> postService.getByIdDto(ID));
        verify(postRepository, times(1)).findById(any());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Successful save with specified ID and user")
    void saveSuccessful() {
        var processedPost = getDefaultPost();

        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(mapperService.map(any(PostDto.class), any())).thenReturn(processedPost);
        when(postRepository.save(any())).thenReturn(processedPost);
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());

        assertNotNull(postService.save(() -> USERNAME, getDefaultPostDto()));
        assertNull(processedPost.getId()); // <- would be set by postRepository, and should be passed as null by saving
        assertFalse(processedPost.getImages().isEmpty());

        verify(authorizationService, times(1)).accessValidation(any(), any());
        verify(postRepository, times(1)).save(any());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Successful save with specified ID and no user")
    void saveSuccessfulWithoutUserId() {
        var processedPost = getDefaultPost();
        processedPost.setUser(null);

        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(mapperService.map(any(PostDto.class), any())).thenReturn(processedPost);
        when(userService.getByPrincipal(any())).thenReturn(getDefaultUser());
        when(postRepository.save(any())).thenReturn(processedPost);
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());

        assertNotNull(postService.save(() -> USERNAME, getDefaultPostDto()));
        assertNull(processedPost.getId());
        assertFalse(processedPost.getImages().isEmpty());
        assertNotNull(processedPost.getUser());

        verify(authorizationService, times(1)).accessValidation(any(), any());
        verify(userService, times(1)).getByPrincipal(any());
        verify(postRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Save fail caused no authentication")
    void saveFailCausedByNoAuthentication() {
        when(authorizationService.requireLogin(any())).thenThrow(ForbiddenPhotogramException.class);
        assertThrows(ForbiddenPhotogramException.class, () -> postService.save(() -> USERNAME, getDefaultPostDto()));
        verify(postRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Save fail caused by forbidden access")
    void saveFailCausedByForbiddenAccess() {
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).accessValidation(any(), any());
        assertThrows(ForbiddenPhotogramException.class, () -> postService.save(() -> USERNAME, getDefaultPostDto()));
        verify(postRepository, times(0)).save(any());
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("Successful update")
    void updateSuccessful() {
        var updatePost = getDefaultPost();
        updatePost.setCaption(SECOND_CAPTION);
        var originalPost = getDefaultPost();
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        when(mapperService.map(any(PostDto.class), any())).thenReturn(updatePost);
        when(postRepository.findById(any())).thenReturn(Optional.of(originalPost));
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());

        assertNotNull(postService.update(() -> USERNAME, ID, getDefaultPostDto()));
        assertEquals(originalPost.getCaption(), updatePost.getCaption());

        verify(mapperService, times(2)).map(any(DataStructure.class), any());
        verify(postRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("Update fail caused by forbidden access")
    void updateFailCausedByForbiddenAccess() {
        when(mapperService.map(any(PostDto.class), any())).thenReturn(getDefaultPost());
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).accessValidation(any(), any());
        assertThrows(ForbiddenPhotogramException.class, () -> postService.update(() -> USERNAME, ID, getDefaultPostDto()));
        verify(postRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Update fail caused by non existing post ID")
    void updateFailCausedByNonExistingId() {
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> postService.update(() -> USERNAME, ID, getDefaultPostDto()));
        verify(postRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Successful delete")
    void deleteSuccessful() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);

        postService.delete(() -> USERNAME, ID);

        verify(postRepository, times(1)).findById(any());
        verify(authorizationService, times(1)).requireLogin(any());
        verify(authorizationService, times(1)).accessValidation(any(), any());
        verify(postRepository, times(1)).delete(any());
    }

    @Test
    @DisplayName("Delete fail caused by non existing id")
    void deleteFailCausedByNonExistingId() {
        when(postRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NotFoundPhotogramException.class, () -> postService.delete(() -> USERNAME, ID));
        verify(postRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Delete fail caused by forbidden access")
    void deleteFailCausedByForbiddenAccess() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(authorizationService.requireLogin(any())).thenReturn(authorizationService);
        doThrow(ForbiddenPhotogramException.class).when(authorizationService).accessValidation(any(), any());
        assertThrows(ForbiddenPhotogramException.class, () -> postService.delete(() -> USERNAME, ID));
        verify(postRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("Successful like")
    void likeSuccessful() {
        reactionTest(PostService.Reactions.LIKE, LIKES + 1, DISLIKES, false);
    }

    private Post reactionTest(final PostService.Reactions reactions, final int expectedLikesCount,
                              final int expectedDislikesCount, final boolean beforeAddLikedPostToUser) {
        var processedPost = getDefaultPost();
        var processedUser = getDefaultUser();

        if (beforeAddLikedPostToUser) {
            processedUser.addLikedPost(processedPost);
            processedUser.addDislikedPost(processedPost);
        }

        when(postRepository.findById(any())).thenReturn(Optional.of(processedPost));
        when(userService.getByPrincipal(any())).thenReturn(processedUser);
        when(postRepository.save(any())).thenReturn(processedPost);
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());

        assertNotNull(postService.react(reactions, () -> USERNAME, ID));

        verify(authorizationService, times(1)).requireLogin(any());
        verify(postRepository, times(1)).save(any());
        verify(userService, times(1)).save(any(), any(), any());

        assertEquals(expectedLikesCount, processedPost.getLikes().intValue());
        assertEquals(expectedDislikesCount, processedPost.getDislikes().intValue());

        return processedPost;
    }

    @Test
    @DisplayName("Successful unlike")
    void unlikeSuccessful() {
        reactionTest(PostService.Reactions.UNLIKE, LIKES - 1, DISLIKES, true);
    }

    @Test
    @DisplayName("Successful dislike")
    void dislikeSuccessful() {
        reactionTest(PostService.Reactions.DISLIKE, LIKES, DISLIKES + 1, false);
    }

    @Test
    @DisplayName("Successful undislike")
    void undislikeSuccessful() {
        reactionTest(PostService.Reactions.UNDISLIKE, LIKES, DISLIKES - 1, true);
    }

    @Test
    @DisplayName("Redundant like")
    void redundantLike() {
        reactionTest(PostService.Reactions.LIKE, LIKES + 1, DISLIKES, false);
        assertThrows(IgnoredPhotogramException.class, () -> postService.react(PostService.Reactions.LIKE, () -> USERNAME, ID));
    }

    @Test
    @DisplayName("Redundant dislike")
    void redundantDislike() {
        reactionTest(PostService.Reactions.DISLIKE, LIKES, DISLIKES + 1, false);
        assertThrows(IgnoredPhotogramException.class, () -> postService.react(PostService.Reactions.DISLIKE, () -> USERNAME, ID));
    }

    @Test
    @DisplayName("Not liked undo like")
    void notLikedUndoLike() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(userService.getByPrincipal(any())).thenReturn(getDefaultUser());
        assertThrows(IgnoredPhotogramException.class, () -> postService.react(PostService.Reactions.UNLIKE, () -> USERNAME, ID));
    }

    @Test
    @DisplayName("Not disliked undo dislike")
    void notDislikedUndoDislike() {
        when(postRepository.findById(any())).thenReturn(Optional.of(getDefaultPost()));
        when(userService.getByPrincipal(any())).thenReturn(getDefaultUser());
        assertThrows(IgnoredPhotogramException.class, () -> postService.react(PostService.Reactions.UNDISLIKE, () -> USERNAME, ID));
    }

    @Test
    @DisplayName("Opposite reaction (dislike) auto remove")
    void oppositeReactionDislikeAutoRemove() {
        var processedPost = getDefaultPost();
        var processedUser = getDefaultUser();
        processedUser.addDislikedPost(processedPost);
        assertTrue(processedUser.hasDislikedPost(processedPost));

        when(postRepository.findById(any())).thenReturn(Optional.of(processedPost));
        when(userService.getByPrincipal(any())).thenReturn(processedUser);
        when(postRepository.save(any())).thenReturn(processedPost);
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());
        postService.react(PostService.Reactions.LIKE, () -> USERNAME, ID);

        assertFalse(processedUser.hasDislikedPost(processedPost));
    }

    @Test
    @DisplayName("Opposite reaction (like) auto remove")
    void oppositeReactionLikeAutoRemove() {
        var processedPost = getDefaultPost();
        var processedUser = getDefaultUser();
        processedUser.addLikedPost(processedPost);
        assertTrue(processedUser.hasLikedPost(processedPost));

        when(postRepository.findById(any())).thenReturn(Optional.of(processedPost));
        when(userService.getByPrincipal(any())).thenReturn(processedUser);
        when(postRepository.save(any())).thenReturn(processedPost);
        when(mapperService.map(any(Post.class), any())).thenReturn(getDefaultPostDto());
        postService.react(PostService.Reactions.DISLIKE, () -> USERNAME, ID);

        assertFalse(processedUser.hasLikedPost(processedPost));
    }

}