package pl.tscript3r.photogram2.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.CommentDto;

import javax.validation.Valid;
import java.security.Principal;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.COMMENT_MAPPING;

@RestController
@RequestMapping(COMMENT_MAPPING)
@RequiredArgsConstructor
public class CommentController {

    @GetMapping
    public Slice<CommentDto> getLatest(@PathVariable("postId") Long postId,
                                       @PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return null;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(Principal principal, @PathVariable("postId") Long postId,
                          @Valid @RequestBody CommentDto commentDto) {
        return null;
    }

    @PutMapping("{id}")
    public CommentDto update(Principal principal, @PathVariable("id") Long id,
                             @Valid @RequestBody CommentDto commentDto) {
        return null;
    }

    @DeleteMapping("{id}")
    public void delete(Principal principal, @PathVariable("id") Long id) {

    }

}
