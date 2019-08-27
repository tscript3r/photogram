package pl.tscript3r.photogram.user.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.tscript3r.photogram.infrastructure.mapper.Dto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Dto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

}
