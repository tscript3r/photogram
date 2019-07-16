package pl.tscript3r.photogram.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import pl.tscript3r.photogram.api.v1.dtos.UserDto;
import pl.tscript3r.photogram.domains.User;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;

public interface UserService {

    User save(@NotNull final User user, @NotNull final Boolean passwordEncode, @NotNull final Boolean addDefaultRole);

    UserDto save(@NotNull final UserDto user);

    UserDto update(@Nullable final Principal principal, @NotNull final Long id, @NotNull final UserDto userDto);

    User update(@NotNull User user);

    void delete(@Nullable final Principal principal, @NotNull final Long id);

    List<UserDto> getAllDto();

    User getByPrincipal(@Nullable final Principal principal);

    User getById(@NotNull final Long id);

    UserDto getByIdDto(@NotNull final Long id);

    User getByUsername(@NotNull final String username);

    UserDto getByUsernameDto(@NotNull final String username);

    User getByEmail(@NotNull final String email);

    UserDto getByEmailDto(@NotNull final String email);

    void resetPassword(@NotNull final String email);

    ResponseEntity<byte[]> getAvatar(@NotNull final Long id);

    void saveAvatar(@Nullable final Principal principal, @NotNull final Long id,
                    @NotNull final MultipartFile multipartFile);

    void confirmEmail(@NotNull final String token);

    Slice<UserDto> getFollowers(@NotNull final Long id, @NotNull final Pageable pageable);

    Slice<UserDto> getFollows(@NotNull final Long id, @NotNull final Pageable pageable);

    void follow(@NotNull final Long id, @NotNull final Long followUserId);

    void unfollow(@NotNull final Long id, @NotNull final Long unfollowUserId);
}
