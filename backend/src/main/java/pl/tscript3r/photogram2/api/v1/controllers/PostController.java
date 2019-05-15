package pl.tscript3r.photogram2.api.v1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.exceptions.controllers.BadRequestPhotogramException;
import pl.tscript3r.photogram2.services.PostService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.*;

@RestController
@RequestMapping(POST_MAPPING)
public class PostController {

    static final String LIKE_MAPPING = "/like";
    static final String UNLIKE_MAPPING = "/unlike";
    static final String DISLIKE_MAPPING = "/dislike";
    static final String UNDISLIKE_MAPPING = "/undislike";

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<PostDto> getLatest(Principal principal,
                                   @RequestParam(value = COUNT_PARAM, required = false) Integer count,
                                   @RequestParam(value = OWN_PARAM, required = false) Boolean ownPosts,
                                   @RequestParam(value = USERNAME_PARAM, required = false) String username) {
        if (isSet(ownPosts) && ownPosts && isSet(username))
            throw new BadRequestPhotogramException("Specify either own param or username param");
        if (isSet(ownPosts) && ownPosts)
            return postService.getLatestUsersPosts(principal, count);
        if (isSet(username))
            return postService.getLatest(username, count);
        return postService.getLatest(count);
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

    @PutMapping
    public PostDto update(Principal principal, @Valid @RequestBody PostDto postDto) {
        return postService.update(principal, postDto);
    }

    @DeleteMapping("{postId}")
    public void delete(Principal principal, @PathVariable("postId") Long postId) {
        postService.delete(principal, postId);
    }

    @PutMapping("{postId}" + LIKE_MAPPING)
    public PostDto like(Principal principal, @PathVariable("postId") Long postId) {
        return postService.like(principal, postId);
    }

    @PutMapping("{postId}" + UNLIKE_MAPPING)
    public PostDto unlike(Principal principal, @PathVariable("postId") Long postId) {
        return postService.unlike(principal, postId);
    }

    @PutMapping("{postId}" + DISLIKE_MAPPING)
    public PostDto dislike(Principal principal, @PathVariable("postId") Long postId) {
        return postService.dislike(principal, postId);
    }

    @PutMapping("{postId}" + UNDISLIKE_MAPPING)
    public PostDto undislike(Principal principal, @PathVariable("postId") Long postId) {
        return postService.undislike(principal, postId);
    }

}
