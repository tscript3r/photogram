package pl.tscript3r.photogram.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.api.v1.dtos.PostDto;
import pl.tscript3r.photogram.exceptions.BadRequestPhotogramException;
import pl.tscript3r.photogram.services.PostService;

import javax.validation.Valid;
import java.security.Principal;

import static pl.tscript3r.photogram.api.v1.controllers.MappingsConsts.*;

@RestController
@RequestMapping(POST_MAPPING)
@RequiredArgsConstructor
public class PostController {

    static final String LIKE_MAPPING = "/like";
    static final String UNLIKE_MAPPING = "/unlike";
    static final String DISLIKE_MAPPING = "/dislike";
    static final String UNDISLIKE_MAPPING = "/undislike";
    static final String UPLOAD_IMAGE_MAPPING = "/upload";
    private static final String IMAGE_ID_VARIABLE = "imageId";
    private static final String IMAGE_ID_PATH_VARIABLE = "{" + IMAGE_ID_VARIABLE + "}";
    static final String GET_IMAGE_MAPPING = "/images/";
    private final PostService postService;

    @GetMapping
    public Slice<PostDto> getLatest(Principal principal,
                                    @PageableDefault(size = 20, sort = ID_VARIABLE, direction = Sort.Direction.DESC) Pageable pageable,
                                    @RequestParam(value = OWN_PARAM, required = false) Boolean ownPosts,
                                    @RequestParam(value = USERNAME_PARAM, required = false) String username) {

        if (isSet(ownPosts) && ownPosts && isSet(username))
            throw new BadRequestPhotogramException("Specify either own param or username param");
        if (isSet(ownPosts) && ownPosts)
            return postService.getLatest(principal, pageable);
        if (isSet(username))
            return postService.getLatest(username, pageable);

        return postService.getLatest(pageable);
    }

    @GetMapping(ID_VARIABLE_MAPPING)
    public PostDto getById(@PathVariable(ID_VARIABLE) Long id) {
        return postService.getByIdDto(id);
    }

    private Boolean isSet(final String input) {
        return (input != null && !input.isEmpty());
    }

    private Boolean isSet(final Boolean input) {
        return input != null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto add(Principal principal, @Valid @RequestBody PostDto postDto) {
        return postService.save(principal, postDto);
    }

    @PutMapping(ID_VARIABLE_MAPPING)
    public PostDto update(Principal principal, @PathVariable(ID_VARIABLE) Long id, @Valid @RequestBody PostDto postDto) {
        return postService.update(principal, id, postDto);
    }

    @DeleteMapping(ID_VARIABLE_MAPPING)
    public void delete(Principal principal, @PathVariable(ID_VARIABLE) Long postId) {
        postService.delete(principal, postId);
    }

    @PutMapping(ID_VARIABLE_MAPPING + LIKE_MAPPING)
    public PostDto like(Principal principal, @PathVariable(ID_VARIABLE) Long postId) {
        return postService.react(PostService.Reactions.LIKE, principal, postId);
    }

    @PutMapping(ID_VARIABLE_MAPPING + UNLIKE_MAPPING)
    public PostDto unlike(Principal principal, @PathVariable(ID_VARIABLE) Long postId) {
        return postService.react(PostService.Reactions.UNLIKE, principal, postId);
    }

    @PutMapping(ID_VARIABLE_MAPPING + DISLIKE_MAPPING)
    public PostDto dislike(Principal principal, @PathVariable(ID_VARIABLE) Long postId) {
        return postService.react(PostService.Reactions.DISLIKE, principal, postId);
    }

    @PutMapping(ID_VARIABLE_MAPPING + UNDISLIKE_MAPPING)
    public PostDto undislike(Principal principal, @PathVariable(ID_VARIABLE) Long postId) {
        return postService.react(PostService.Reactions.UNDISLIKE, principal, postId);
    }

    @PostMapping(ID_VARIABLE_MAPPING + UPLOAD_IMAGE_MAPPING)
    public PostDto uploadImage(Principal principal, @PathVariable(ID_VARIABLE) Long id,
                               @RequestParam("image") MultipartFile imageFile) {
        return postService.saveImage(principal, id, imageFile);
    }

    @GetMapping(ID_VARIABLE_MAPPING + GET_IMAGE_MAPPING + IMAGE_ID_PATH_VARIABLE)
    public ResponseEntity<byte[]> getImage(@PathVariable(ID_VARIABLE) Long id,
                                           @PathVariable(IMAGE_ID_VARIABLE) Long imageId) {
        return postService.getImage(id, imageId);
    }

}
