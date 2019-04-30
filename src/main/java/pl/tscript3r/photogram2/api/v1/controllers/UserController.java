package pl.tscript3r.photogram2.api.v1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.exceptions.controllers.BadRequestPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.Valid;

import java.security.Principal;
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
    public UserDto findBy(@RequestParam(value = "username", defaultValue = "") String username,
                               @RequestParam(value = "email", defaultValue = "") String email,
                               @RequestParam(value = "id", required = false) Long id) {
        if(isSet(username))
            return userService.getByUsernameDto(username);
        if(isSet(email))
            return userService.getByEmailDto(email);
        if(id != null)
            return userService.getByIdDto(id);

        throw new BadRequestPhotogramException("400: bad request. Specify any of parameter: id / email / username");
    }

    private Boolean isSet(String username) {
        return (username != null && !username.isEmpty());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        return userService.save(userDto);
    }

    @PutMapping
    public UserDto updateUser(Principal principal, @Valid @RequestBody UserDto userDto) {
        return userService.update(principal, userDto);
    }

}
