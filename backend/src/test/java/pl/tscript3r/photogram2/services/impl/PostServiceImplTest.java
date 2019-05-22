package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.repositories.PostRepository;
import pl.tscript3r.photogram2.services.ImageService;
import pl.tscript3r.photogram2.services.PostService;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import static pl.tscript3r.photogram2.api.v1.services.impl.MapperServiceImplTest.getInstance;

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

    private PostService postService;

    @BeforeEach
    void setUp() {
        var mapperService = getInstance();
        postService = new PostServiceImpl(userService, roleService, postRepository, imageService, mapperService);
    }

    @Test
    void getLatest() {
    }

    @Test
    void getLatest1() {
    }

    @Test
    void getLatest2() {
    }

    @Test
    void getById() {
    }

    @Test
    void getByIdDto() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void react() {
    }
}