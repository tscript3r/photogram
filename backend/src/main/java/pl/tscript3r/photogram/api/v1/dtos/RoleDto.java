package pl.tscript3r.photogram.api.v1.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto implements Dto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String name;

}
