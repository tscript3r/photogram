package pl.tscript3r.photogram2.api.v1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.exceptions.services.UserNotFoundPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.USER_MAPPING;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getDefaultUserDto;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@WithMockUser("spring")
class UserControllerTestIT {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("Get all users")
    void getAll() throws Exception {
        Set<UserDto> inputUsers = Collections.singleton(getDefaultUserDto());
        when(userService.getAllDto()).thenReturn(inputUsers);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        TypeReference<Set<UserDto>> typeReference = new TypeReference<>() {
        };
        Set<UserDto> outputUsers = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
        UserDto expected = inputUsers.iterator().next();
        UserDto actual = outputUsers.iterator().next();
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertNull(actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getBio(), actual.getBio());
    }

    @Test
    @DisplayName("Get by ID not found")
    void getByIdNotFound() throws Exception {
        when(userService.getByIdDto(any())).thenThrow(UserNotFoundPhotogramException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get by ID")
    void getByIdFound() throws Exception {
        UserDto input = getDefaultUserDto();
        when(userService.getByIdDto(any())).thenReturn(getDefaultUserDto());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getId(), output.getId());
        assertEquals(input.getEmail(), output.getEmail());
    }

    @Test
    @DisplayName("Find by nothing given")
    void findByNothing() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @DisplayName("Find by username")
    void findByUsername() throws Exception {
        UserDto input = getDefaultUserDto();
        when(userService.getByUsernameDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?username=any")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getUsername(), output.getUsername());
    }

    @Test
    @DisplayName("Find by id")
    void findById() throws Exception {
        UserDto input = getDefaultUserDto();
        when(userService.getByIdDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getId(), output.getId());
    }

    @Test
    @DisplayName("Find by email")
    void findByEmail() throws Exception {
        UserDto input = getDefaultUserDto();
        when(userService.getByEmailDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?email=any@any.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getEmail(), output.getEmail());
    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }
}