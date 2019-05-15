package pl.tscript3r.photogram2.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.services.NotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.UserRepository;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MapperService mapperService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder,
                           MapperService mapperService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mapperService = mapperService;
    }

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
    public UserDto update(final Principal principal, final UserDto userDto) {
        // TODO: access permissions check?
        var existingUser = getExistingUser(userDto);
        updateValuesAndSave(existingUser, userDto);
        return mapperService.map(existingUser, UserDto.class);
    }

    private User getExistingUser(final UserDto userDto) {
        if (userDto.getId() != null)
            return getById(userDto.getId());
        if (userDto.getEmail() != null)
            return getByEmail(userDto.getEmail());
        throw new NotFoundPhotogramException("Specify id or email");
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
        return null;
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
    public void delete(final Long id) {
        if (!userRepository.existsById(id))
            throw new NotFoundPhotogramException(String.format("Given user id=%s not exists", id.toString()));
        else
            userRepository.deleteById(id);
    }

    @Override
    public void resetPassword(final String email) {

    }

}
