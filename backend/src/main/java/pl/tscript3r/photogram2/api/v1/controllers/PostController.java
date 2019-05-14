package pl.tscript3r.photogram2.api.v1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.PostDto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.POST_MAPPING;

@RestController
@RequestMapping(POST_MAPPING)
public class PostController {

    @GetMapping
    public List<PostDto> getLatest(@RequestParam(value = "count", required = false) Integer count,
                                   @RequestParam(value = "own", required = false) Boolean ownPosts,
                                   @RequestParam(value = "username", required = false) String username) {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto add(@Valid @RequestBody PostDto postDto) {
        return null;
    }

    @PutMapping
    public PostDto update(Principal principal, @Valid @RequestBody PostDto postDto) {
        return null;
    }

    @DeleteMapping("{postId}")
    public void delete(@PathVariable("postId") Long postId) {

    }

    @PutMapping("{postId}/like")
    public PostDto like(@PathVariable("postId") Long postId) {
        return null;
    }

    @PutMapping("{postId}/unlike")
    public PostDto unlike(@PathVariable("postId") Long postId) {
        return null;
    }

    @PutMapping("{postId}/dislike")
    public PostDto dislike(@PathVariable("postId") Long postId) {
        return null;
    }

    @PutMapping("{postId}/undislike")
    public PostDto undislike(@PathVariable("postId") Long postId) {
        return null;
    }

}
