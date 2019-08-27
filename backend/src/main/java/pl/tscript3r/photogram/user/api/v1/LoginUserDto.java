package pl.tscript3r.photogram.user.api.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tscript3r.photogram.infrastructure.mapper.Dto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserDto implements Dto {

    private String username;
    private String password;

}
