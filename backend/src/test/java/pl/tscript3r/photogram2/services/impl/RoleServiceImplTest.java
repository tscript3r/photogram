package pl.tscript3r.photogram2.services.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.tscript3r.photogram2.exceptions.services.RoleNotFoundPhotogramException;
import pl.tscript3r.photogram2.repositories.RoleRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.tscript3r.photogram2.Consts.SECOND_ROLE;
import static pl.tscript3r.photogram2.domains.RoleTest.getDefaultRole;
import static pl.tscript3r.photogram2.domains.RoleTest.getSecondRole;

@DisplayName("Role service")
@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleServiceImpl roleService;

    @Test
    @DisplayName("Get default role")
    void getDefault() {
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(getDefaultRole()));
        assertNotNull(roleService.getDefault());
    }

    @Test
    @DisplayName("Get existing by firstname")
    void getByName() {
        when(roleRepository.findByName(any())).thenReturn(Optional.of(getSecondRole()));
        assertNotNull(roleService.getByName(SECOND_ROLE));
    }

    @Test
    @DisplayName("Get non existing by firstname")
    void getNonExistingByName() {
        when(roleRepository.findByName(any())).thenThrow(RoleNotFoundPhotogramException.class);
        assertThrows(RoleNotFoundPhotogramException.class, () -> roleService.getByName(""));
    }

}