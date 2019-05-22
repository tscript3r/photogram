package pl.tscript3r.photogram2.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;
import pl.tscript3r.photogram2.exceptions.BadRequestPhotogramException;
import pl.tscript3r.photogram2.services.PostService;

import javax.validation.Valid;
import java.security.Principal;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.*;

@RestController
@RequestMapping(POST_MAPPING)
@RequiredArgsConstructor
public class PostController {

    static final String LIKE_MAPPING = "/like";
    static final String UNLIKE_MAPPING = "/unlike";
    static final String DISLIKE_MAPPING = "/dislike";
    static final String UNDISLIKE_MAPPING = "/undislike";

    private final PostService postService;

    @GetMapping
    public Slice<PostDto> getLatest(Principal principal,
                                    @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
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

    @GetMapping("{id}")
    public PostDto getById(@PathVariable("id") Long id) {
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

    @PutMapping("{id}")
    public PostDto update(Principal principal, @PathVariable("id") Long id, @Valid @RequestBody PostDto postDto) {
        return postService.update(principal, id, postDto);
    }

    @DeleteMapping("{id}")
    public void delete(Principal principal, @PathVariable("id") Long postId) {
        postService.delete(principal, postId);
    }

    @PutMapping("{id}" + LIKE_MAPPING)
    public PostDto like(Principal principal, @PathVariable("id") Long postId) {
        return postService.react(PostService.Reactions.LIKE, principal, postId);
    }

    @PutMapping("{id}" + UNLIKE_MAPPING)
    public PostDto unlike(Principal principal, @PathVariable("id") Long postId) {
        return postService.react(PostService.Reactions.UNLIKE, principal, postId);
    }

    @PutMapping("{id}" + DISLIKE_MAPPING)
    public PostDto dislike(Principal principal, @PathVariable("id") Long postId) {
        return postService.react(PostService.Reactions.DISLIKE, principal, postId);
    }

    @PutMapping("{id}" + UNDISLIKE_MAPPING)
    public PostDto undislike(Principal principal, @PathVariable("id") Long postId) {
        return postService.react(PostService.Reactions.UNDISLIKE, principal, postId);
    }

}
