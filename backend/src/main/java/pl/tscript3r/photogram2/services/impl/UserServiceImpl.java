package pl.tscript3r.photogram2.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.api.v1.services.MapperService;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.exceptions.services.UserNotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.UserRepository;
import pl.tscript3r.photogram2.services.RoleService;
import pl.tscript3r.photogram2.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Service
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
    public User save(User user) {
        user.addRole(roleService.getDefault());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDto save(UserDto userDto) {
        // TODO: confirmation mail needs to be send here
        User user = mapperService.map(userDto, User.class);
        return mapperService.map(save(user), UserDto.class);
    }

    @Override
    public UserDto update(Principal principal, UserDto userDto) {
        // TODO: access permissions check?
        User existingUser = getExistingUser(userDto);
        updateValues(existingUser, userDto);
        save(existingUser);
        return mapperService.map(existingUser, UserDto.class);
    }

    private User getExistingUser(UserDto userDto) {
        if (userDto.getId() != null)
            return getById(userDto.getId());
        if (userDto.getEmail() != null)
            return getByEmail(userDto.getEmail());
        throw new UserNotFoundPhotogramException("Specify id or email");
    }

    private void updateValues(User existingUser, UserDto userDto) {
        if (userDto.getEmail() != null &&
                !existingUser.getEmail().equalsIgnoreCase(userDto.getEmail())) {
            existingUser.setEmail(userDto.getEmail());
            existingUser.setEmailConfirmed(false);
            // TODO: confirmation mail needs to be send here
        }
        if (userDto.getBio() != null)
            existingUser.setBio(userDto.getBio());
        if (userDto.getName() != null)
            existingUser.setName(userDto.getName());
        if (userDto.getPassword() != null)
            existingUser.setPassword(userDto.getPassword());
        if (userDto.getUsername() != null)
            existingUser.setUsername(userDto.getUsername());
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public Set<UserDto> getAllDto() {
        return new HashSet<>(mapperService.map(userRepository.findAll(), UserDto.class));
    }

    @Override
    public User getByPrincipal(Principal principal) {
        return null;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundPhotogramException(String.format("User id=%s not found", id)));
    }

    @Override
    public UserDto getByIdDto(Long id) {
        return mapperService.map(getById(id), UserDto.class);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UserNotFoundPhotogramException(String.format("Username=%s not found", username)));
    }

    @Override
    public UserDto getByUsernameDto(String username) {
        return mapperService.map(getByUsername(username), UserDto.class);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundPhotogramException(String.format("Given email=%s not found", email)));
    }

    @Override
    public UserDto getByEmailDto(String email) {
        return mapperService.map(getByEmail(email), UserDto.class);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id))
            throw new UserNotFoundPhotogramException(String.format("Given user id=%s not exists", id.toString()));
        else
            userRepository.deleteById(id);
    }
}
