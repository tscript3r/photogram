package pl.tscript3r.photogram2.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.exceptions.BadRequestPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.*;

@RestController
@RequestMapping(USER_MAPPING)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAllDto();
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getByIdDto(id);
    }

    @GetMapping("find")
    public UserDto findBy(@RequestParam(value = USERNAME_PARAM, required = false) String username,
                          @RequestParam(value = EMAIL_PARAM, required = false) String email,
                          @RequestParam(value = ID_PARAM, required = false) Long id) {
        if (isSet(username))
            return userService.getByUsernameDto(username);
        if (isSet(email))
            return userService.getByEmailDto(email);
        if (id != null)
            return userService.getByIdDto(id);
        throw new BadRequestPhotogramException("Bad request - specify any of: id / email / username");
    }

    private Boolean isSet(final String username) {
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

    private void checkUpdateValidationErrors(final BindingResult bindingResult) {
        var objectErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : objectErrors)
            if (objectError.getCode() != null) {
                String errorCode = objectError.getCode();
                if (!errorCode.equalsIgnoreCase("NotEmpty"))
                    throw new BadRequestPhotogramException("Some of the given values are incorrect");
                // TODO: probably to refactor?
            }
    }

    private void checkIdAndEmail(final UserDto userDto) {
        if (userDto.getId() == null && userDto.getEmail() == null)
            throw new BadRequestPhotogramException("Specify id or/and email");
    }

    @DeleteMapping("{id}")
    public void delete(Principal principal, @PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("reset")
    public void resetPassword(@RequestParam(value = "email") String email) {
        userService.resetPassword(email);
    }

}