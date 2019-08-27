package pl.tscript3r.photogram.user;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.infrastructure.exception.ForbiddenPhotogramException;
import pl.tscript3r.photogram.infrastructure.exception.IgnoredPhotogramException;
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.infrastructure.mapper.MapperService;
import pl.tscript3r.photogram.post.image.ImageService;
import pl.tscript3r.photogram.user.api.v1.UserDto;
import pl.tscript3r.photogram.user.email.EmailConfirmation;
import pl.tscript3r.photogram.user.email.EmailService;
import pl.tscript3r.photogram.user.role.RoleService;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final AuthorizationService authorizationService;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final EmailService emailService;
    private final MapperService mapperService;

    public User save(final User user, final Boolean passwordEncode, final Boolean addDefaultRole) {
        if (addDefaultRole)
            user.addRole(roleService.getDefault());
        if (passwordEncode)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = userRepository.save(user);
        savedUser.setEmailConfirmation(emailConfirmation(savedUser));
        return savedUser;
    }

    private EmailConfirmation emailConfirmation(final User user) {
        return emailService.createEmailConfirmation(user, true);
    }

    public UserDto save(final UserDto userDto) {
        var user = mapperService.map(userDto, User.class);
        return mapperService.map(save(user, true, true), UserDto.class);
    }

    public UserDto update(final Principal principal, final Long id, final UserDto userDto) {
        authorizationService.requireLogin(principal)
                .accessValidation(principal, id);
        var existingUser = getById(id);
        updateValuesAndSave(existingUser, userDto);
        return mapperService.map(existingUser, UserDto.class);
    }

    public User update(@NotNull final User user) {
        return userRepository.save(user);
    }

    private void updateValuesAndSave(final User existingUser, final UserDto userDto) {
        boolean passwordEncode = false;
        boolean sendEmailConfirmation = false;

        if (userDto.getEmail() != null &&
                !existingUser.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            existingUser.setEmail(userDto.getEmail());
            sendEmailConfirmation = true;
        }
        if (userDto.getBio() != null)
            existingUser.setBio(userDto.getBio());
        if (userDto.getFirstname() != null)
            existingUser.setFirstname(userDto.getFirstname());
        if (userDto.getPassword() != null) {
            passwordEncode = true;
            existingUser.setPassword(userDto.getPassword());
        }
        if (userDto.getUsername() != null)
            existingUser.setUsername(userDto.getUsername());

        saveUpdated(existingUser, passwordEncode, sendEmailConfirmation);
    }

    private User saveUpdated(final User user, final Boolean passwordEncode, final Boolean emailConfirmation) {
        if (passwordEncode)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (emailConfirmation)
            emailService.createEmailConfirmation(user, true);
        return userRepository.save(user);
    }

    public List<UserDto> getAllDto() {
        return mapperService.map(userRepository.findAll(), UserDto.class);
    }

    public User getByPrincipal(final Principal principal) {
        if (principal == null)
            throw new ForbiddenPhotogramException("Login in order to access this resource");
        return getByUsername(principal.getName());
    }

    public User getById(final Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("User id=%s not found", id)));
    }

    public UserDto getByIdDto(final Long id) {
        return mapperService.map(getById(id), UserDto.class);
    }

    public User getByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Username=%s not found", username)));
    }

    public UserDto getByUsernameDto(final String username) {
        return mapperService.map(getByUsername(username), UserDto.class);
    }

    public User getByEmail(final String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Given email=%s not found", email)));
    }

    public UserDto getByEmailDto(final String email) {
        return mapperService.map(getByEmail(email), UserDto.class);
    }

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

    public void resetPassword(@NotNull final String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundPhotogramException(String.format("Given email=%s not found", email)));
        var newPassword = RandomStringUtils.randomAlphanumeric(8);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        emailService.sendNewPassword(user, newPassword);
    }

    public ResponseEntity<byte[]> getAvatar(@NotNull final Long id) {
        getById(id); // <-- checking if the given user id is existing
        return imageService.getAvatar(id);
    }

    public void saveAvatar(final Principal principal, @NotNull final Long id,
                           @NotNull final MultipartFile multipartFile) {
        authorizationService.requireLogin(principal)
                .accessValidation(principal, id);
        getById(id); // <-- checking if the given user id is existing
        imageService.saveAvatar(id, multipartFile);
    }

    public void confirmEmail(@NotNull final String token) {
        emailService.setEmailConfirmed(token);
    }

    public Slice<UserDto> getFollowers(@NotNull final Long id, @NotNull final Pageable pageable) {
        return userRepository.findByFollowed(getById(id), pageable)
                .map(user -> mapperService.map(user, UserDto.class));
    }

    public Slice<UserDto> getFollows(@NotNull final Long id, @NotNull final Pageable pageable) {
        return userRepository.findByFollows(getById(id), pageable)
                .map(user -> mapperService.map(user, UserDto.class));
    }

    public void follow(final Principal principal, @NotNull final Long followUserId) {
        var user = getByPrincipal(principal);
        var followUser = getById(followUserId);
        if (!user.isFollowing(followUser)) {
            followUser.addFollowedBy(user);
            user.follow(followUser);
            userRepository.save(user);
            userRepository.save(followUser);
        } else
            throw new IgnoredPhotogramException(String.format("Already following user id=%d, ignored", followUserId));
    }

    public void unfollow(final Principal principal, @NotNull final Long unfollowUserId) {
        var user = getByPrincipal(principal);
        var unfollowUser = getById(unfollowUserId);
        if (user.isFollowing(unfollowUser)) {
            user.unfollow(unfollowUser);
            unfollowUser.removeFollowedBy(user);
            userRepository.save(user);
            userRepository.save(unfollowUser);
        } else
            throw new IgnoredPhotogramException(String.format("Did not follow user id=%d, ignored", unfollowUserId));
    }

}
