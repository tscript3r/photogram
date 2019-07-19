package pl.tscript3r.photogram.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram.api.v1.dtos.CommentDto;
import pl.tscript3r.photogram.services.CommentService;

import javax.validation.Valid;
import java.security.Principal;

import static pl.tscript3r.photogram.api.v1.controllers.MappingsConsts.*;

@RestController
@RequestMapping(COMMENT_MAPPING)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping(ID_VARIABLE_MAPPING)
    public Slice<CommentDto> getLatest(@PathVariable(ID_POST_VARIABLE) Long postId,
                                       @PageableDefault(size = 5, sort = ID_VARIABLE, direction = Sort.Direction.DESC) Pageable pageable) {
        return commentService.getLatest(postId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto add(Principal principal, @PathVariable(ID_POST_VARIABLE) Long postId,
                          @Valid @RequestBody CommentDto commentDto) {
        return commentService.save(principal, postId, commentDto);
    }

    @PutMapping((ID_VARIABLE_MAPPING))
    public CommentDto update(Principal principal, @PathVariable(ID_VARIABLE) Long id,
                             @Valid @RequestBody CommentDto commentDto) {
        return commentService.update(principal, id, commentDto);
    }

    @DeleteMapping((ID_VARIABLE_MAPPING))
    public void delete(Principal principal, @PathVariable(ID_VARIABLE) Long id) {
        commentService.delete(principal, id);
    }

}
