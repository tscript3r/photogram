package pl.tscript3r.photogram2.api.v1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.exceptions.controllers.BadRequestPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.USER_MAPPING;

@RestController
@RequestMapping(USER_MAPPING)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Set<UserDto> getAll() {
        return userService.getAllDto();
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getByIdDto(id);
    }

    @GetMapping("find")
    public UserDto findBy(@RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "email", required = false) String email,
                          @RequestParam(value = "id", required = false) Long id) {
        if (isSet(username))
            return userService.getByUsernameDto(username);
        if (isSet(email))
            return userService.getByEmailDto(email);
        if (id != null)
            return userService.getByIdDto(id);

        throw new BadRequestPhotogramException("Bad request - specify any of: id / email / username");
    }

    private Boolean isSet(String username) {
        return (username != null && !username.isEmpty());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PutMapping
    public UserDto update(Principal principal, @Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        checkUpdateValidationErrors(bindingResult);
        checkIdAndEmail(userDto);
        return userService.update(principal, userDto);
    }

    private void checkUpdateValidationErrors(BindingResult bindingResult) {
        List<ObjectError> objectErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : objectErrors)
            if (objectError.getCode() != null) {
                String errorCode = objectError.getCode();
                if (!errorCode.equalsIgnoreCase("NotEmpty"))
                    throw new BadRequestPhotogramException("Some of the given values are incorrect.");
                // TODO: probably to refactor?
            }
    }

    private void checkIdAndEmail(UserDto userDto) {
        if (userDto.getId() == null && userDto.getEmail() == null)
            throw new BadRequestPhotogramException("Specify id or/and email");
    }

    @DeleteMapping("{id}")
    public void delete(Principal principal, @PathVariable Long id) {
        userService.delete(id);
    }

}