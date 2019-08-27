package pl.tscript3r.photogram.user.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.infrastructure.exception.BadRequestPhotogramException;
import pl.tscript3r.photogram.user.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.tscript3r.photogram.infrastructure.MappingsConsts.*;

@RestController
@RequestMapping(USER_MAPPING)
@RequiredArgsConstructor
public class UserController {

    public static final String FIND_MAPPING = "find";
    public static final String PASSWORD_RESET_MAPPING = "/reset_password";
    public static final String AVATAR_MAPPING = "/avatar";
    public static final String EMAIL_CONFIRMATION_MAPPING = "/confirm_email";
    public static final String FOLLOWERS_MAPPING = "/followers";
    public static final String FOLLOWS_MAPPING = "/follows";
    public static final String FOLLOW_MAPPING = "/follow";
    public static final String UNFOLLOW_MAPPING = "/unfollow";
    public static final String TOKEN_PARAM = "token";
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
    public UserDto getBy(@RequestParam(value = USERNAME_PARAM, required = false) String username,
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
        return userService.update(principal, id, userDto);
    }

    private void checkUpdateValidationErrors(final BindingResult bindingResult) {
        // in this case NotEmpty constrain can be ignored, because the client does not have to send all values
        var objectErrors = bindingResult.getAllErrors();
        for (ObjectError objectError : objectErrors)
            if (objectError.getCode() != null) {
                String errorCode = objectError.getCode();
                if (!errorCode.equalsIgnoreCase("NotEmpty"))
                    throw new BadRequestPhotogramException("Some of the given values are incorrect");
            }
    }

    @DeleteMapping(ID_VARIABLE_MAPPING)
    public void delete(Principal principal, @PathVariable Long id) {
        userService.delete(principal, id);
    }

    @PutMapping(PASSWORD_RESET_MAPPING)
    public void passwordReset(@RequestParam(value = EMAIL_PARAM) String email) {
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

    @PutMapping(EMAIL_CONFIRMATION_MAPPING)
    public void confirmEmail(@RequestParam(value = TOKEN_PARAM) @Valid @UUID String token) {
        userService.confirmEmail(token);
    }

    @GetMapping(ID_VARIABLE_MAPPING + FOLLOWERS_MAPPING)
    public Slice<UserDto> getFollowers(@PathVariable Long id, @PageableDefault(size = 5, sort = ID_VARIABLE,
            direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getFollowers(id, pageable);
    }

    @GetMapping(ID_VARIABLE_MAPPING + FOLLOWS_MAPPING)
    public Slice<UserDto> getFollows(@PathVariable Long id, @PageableDefault(size = 30, sort = ID_VARIABLE,
            direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getFollows(id, pageable);
    }

    @PutMapping(FOLLOW_MAPPING + "/" + ID_VARIABLE_MAPPING)
    public void follow(Principal principal, @PathVariable(ID_VARIABLE) Long followUserId) {
        userService.follow(principal, followUserId);
    }

    @PutMapping(UNFOLLOW_MAPPING + "/" + ID_VARIABLE_MAPPING)
    public void unfollow(Principal principal, @PathVariable(ID_VARIABLE) Long unfollowUserId) {
        userService.unfollow(principal, unfollowUserId);
    }

}