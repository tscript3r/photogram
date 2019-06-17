package pl.tscript3r.photogram2.api.v1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    private static final String FIND_MAPPING = "find";
    public static final String PASSWORD_RESET_MAPPING = "reset";
    public static final String AVATAR_MAPPING = "/avatar";
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAllDto();
    }

    @GetMapping(ID_VARIABLE_MAPPING)
    public UserDto getById(@PathVariable Long id) {
        return userService.getByIdDto(id);
    }

    @GetMapping(FIND_MAPPING)
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

    @PutMapping(ID_VARIABLE_MAPPING)
    public UserDto update(Principal principal, @PathVariable("id") Long id, @Valid @RequestBody UserDto userDto,
                          BindingResult bindingResult) {
        checkUpdateValidationErrors(bindingResult);
        checkIdAndEmail(userDto);
        return userService.update(principal, id, userDto);
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

    @DeleteMapping(ID_VARIABLE_MAPPING)
    public void delete(Principal principal, @PathVariable Long id) {
        userService.delete(principal, id);
    }

    @GetMapping(PASSWORD_RESET_MAPPING)
    public void resetPassword(@RequestParam(value = "email") String email) {
        userService.resetPassword(email);
    }

    @GetMapping(ID_VARIABLE_MAPPING + AVATAR_MAPPING)
    public ResponseEntity<byte[]> getAvatar(@PathVariable(ID_VARIABLE) Long id) {
        return userService.getAvatar(id);
    }

    @PostMapping(ID_VARIABLE_MAPPING + AVATAR_MAPPING)
    public void uploadAvatar(Principal principal, @PathVariable(ID_VARIABLE) Long id,
                             @RequestParam("image") MultipartFile imageFile) {
        userService.saveAvatar(principal, id, imageFile);
    }

}