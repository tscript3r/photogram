package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.ForbiddenPhotogramException;
import pl.tscript3r.photogram2.exceptions.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.UserRepository;
import pl.tscript3r.photogram2.services.AuthorizationService;
import pl.tscript3r.photogram2.services.ImageService;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final MapperService mapperService;

    @Override
    public User save(final User user, final Boolean passwordEncode, final Boolean addDefaultRole) {
        if (addDefaultRole)
            user.addRole(roleService.getDefault());
        if (passwordEncode)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDto save(final UserDto userDto) {
        // TODO: confirmation mail needs to be send here
        var user = mapperService.map(userDto, User.class);
        return mapperService.map(save(user, true, true), UserDto.class);
    }

    @Override
    public UserDto update(final Principal principal, final Long id, final UserDto userDto) {
        authorizationService.requireLogin(principal)
                .accessValidation(principal, userDto.getId());
        var existingUser = getById(id);
        updateValuesAndSave(existingUser, userDto);
        return mapperService.map(existingUser, UserDto.class);
    }

    private void updateValuesAndSave(final User existingUser, final UserDto userDto) {
        boolean passwordEncode = false;

        if (userDto.getEmail() != null &&
                !existingUser.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            existingUser.setEmail(userDto.getEmail());
            existingUser.setEmailConfirmed(false);
            // TODO: confirmation mail needs to be send here
        }
        if (userDto.getBio() != null)
            existingUser.setBio(userDto.getBio());
        if (userDto.getName() != null)
            existingUser.setFirstname(userDto.getName());
        if (userDto.getPassword() != null) {
            passwordEncode = true;
            existingUser.setPassword(userDto.getPassword());
        }
        if (userDto.getUsername() != null)
            existingUser.setUsername(userDto.getUsername());

        save(existingUser, passwordEncode, false);
    }

    @Override
    public List<UserDto> getAllDto() {
        return mapperService.map(userRepository.findAll(), UserDto.class);
    }

    @Override
    public User getByPrincipal(final Principal principal) {
        if (principal == null)
            throw new ForbiddenPhotogramException("Login in order to access this resource");
        return getByUsername(principal.getName());
    }

    @Override
    public User getById(final Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("User id=%s not found", id)));
    }

    @Override
    public UserDto getByIdDto(final Long id) {
        return mapperService.map(getById(id), UserDto.class);
    }

    @Override
    public User getByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Username=%s not found", username)));
    }

    @Override
    public UserDto getByUsernameDto(final String username) {
        return mapperService.map(getByUsername(username), UserDto.class);
    }

    @Override
    public User getByEmail(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Given email=%s not found", email)));
    }

    @Override
    public UserDto getByEmailDto(final String email) {
        return mapperService.map(getByEmail(email), UserDto.class);
    }

    @Override
    public void delete(final Principal principal, @NotNull final Long id) {
        authorizationService.requireLogin(principal)
                .accessValidation(principal, id);
        if (!userRepository.existsById(id))
            throw getUserNotFoundException(id);
        else
            userRepository.deleteById(id);
    }

    private NotFoundPhotogramException getUserNotFoundException(final Long id) {
        return new NotFoundPhotogramException(String.format("Given user id=%s not exists", id.toString()));
    }

    @Override
    public void resetPassword(@NotNull final String email) {

    }

    @Override
    public ResponseEntity<byte[]> getAvatar(@NotNull final Long id) {
        getById(id); // <-- checking if the given user id is existing
        return imageService.getAvatar(id);
    }

    @Override
    public void saveAvatar(final Principal principal, @NotNull final Long id,
                           @NotNull final MultipartFile multipartFile) {
        authorizationService.requireLogin(principal)
                .accessValidation(principal, id);
        getById(id); // <-- checking if the given user id is existing
        imageService.saveAvatar(id, multipartFile);
    }

}
