package pl.tscript3r.photogram2.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Dto {

    @Nullable
    private Long id;

    @NotEmpty
    @Size(min = 4, max = 16)
    private String name;

    @NotEmpty
    @Size(min = 4, max = 16)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    @Size(min = 6, max = 32)
    private String password;

    @NotEmpty
    @Email
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean emailConfirmed = false;

    @Nullable
    @Size(max = 900)
    private String bio;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Nullable
    private LocalDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Nullable
    private Set<RoleDto> roles = new HashSet<>();

}
