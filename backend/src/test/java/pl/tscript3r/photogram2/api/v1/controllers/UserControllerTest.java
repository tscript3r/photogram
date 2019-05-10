package pl.tscript3r.photogram2.api.v1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.photogram2.api.v1.dtos.UserDto;
import pl.tscript3r.photogram2.exceptions.services.UserNotFoundPhotogramException;
import pl.tscript3r.photogram2.services.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram2.Consts.*;
import static pl.tscript3r.photogram2.api.v1.controllers.MappingsConsts.USER_MAPPING;
import static pl.tscript3r.photogram2.api.v1.dtos.UserDtoTest.getDefaultUserDto;

@DisplayName("Users controller")
@WebMvcTest(UserController.class)
@WithMockUser("spring")
class UserControllerTest {

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String USERNAME_KEY = "username";
    private static final String NAME_KEY = "name";
    private static final String ID_KEY = "id";

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
        var inputUsers = Collections.singletonList(getDefaultUserDto());
        when(userService.getAllDto()).thenReturn(inputUsers);
        var result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING)
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
        var input = getDefaultUserDto();
        when(userService.getByIdDto(any())).thenReturn(getDefaultUserDto());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
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
        var input = getDefaultUserDto();
        when(userService.getByUsernameDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?username=any")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getUsername(), output.getUsername());
    }

    @Test
    @DisplayName("Find by id")
    void findById() throws Exception {
        var input = getDefaultUserDto();
        when(userService.getByIdDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?id=1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getId(), output.getId());
    }

    @Test
    @DisplayName("Find by email")
    void findByEmail() throws Exception {
        var input = getDefaultUserDto();
        when(userService.getByEmailDto(any())).thenReturn(input);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/find?email=any@any.com")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        var output = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(input.getEmail(), output.getEmail());
    }

    @Test
    @DisplayName("Add valid DTO")
    void addValidUserDto() throws Exception {
        performPostMockMvc(status().isCreated(), getValuesMapForJson());
        verify(userService, times(1)).save(any(UserDto.class));
    }

    private void performPostMockMvc(ResultMatcher matcher, Map<String, String> valuesMap) throws Exception {
        performMockMvc(MockMvcRequestBuilders.post(USER_MAPPING), matcher, valuesMap);
    }

    private void performPutMockMvc(ResultMatcher matcher, Map<String, String> valuesMap) throws Exception {
        performMockMvc(MockMvcRequestBuilders.put(USER_MAPPING), matcher, valuesMap);
    }

    private void performMockMvc(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, ResultMatcher matcher,
                                Map<String, String> valuesMap) throws Exception {
        mockMvc.perform(mockHttpServletRequestBuilder
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(valuesMap)))
                .andExpect(matcher);
    }

    private Map<String, String> getValuesMapForJson() {
        Map<String, String> values = new HashMap<>();
        values.put(USERNAME_KEY, USERNAME);
        values.put(PASSWORD_KEY, PASSWORD);
        values.put(NAME_KEY, NAME);
        values.put(EMAIL_KEY, EMAIL);
        values.put("bio", BIO);
        return values;
    }

    @Test
    @DisplayName("Add invalid (to short password) DTO")
    void addInvalidPasswordShortDto() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(PASSWORD_KEY, "12345")); // min 6
        verify(userService, times(0)).save(any(UserDto.class));
    }

    private Map<String, String> getValuesMapForJson(String editedKey, String newValue) {
        var result = getValuesMapForJson();
        result.put(editedKey, newValue);
        return result;
    }

    @Test
    @DisplayName("Add invalid (to long password) DTO")
    void addInvalidPasswordLongDto() throws Exception {
        performPostMockMvc(status().isBadRequest(),
                getValuesMapForJson(PASSWORD_KEY, "1234567890_1234567890_1234567890_")); // max 32, is 33
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without password) DTO")
    void addInvalidDtoWithoutPassword() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(PASSWORD_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    private Map<String, String> getValuesMapForJson(String withoutKey) {
        var result = getValuesMapForJson();
        result.remove(withoutKey);
        return result;
    }

    @Test
    @DisplayName("Add invalid (email) DTO")
    void addInvalidEmailDto() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(EMAIL_KEY, "invalidEmail.com"));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without email) DTO")
    void addInvalidDtoWithoutEmail() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(EMAIL_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to short username) DTO")
    void addInvalidDtoWithoutToShortUsername() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(USERNAME_KEY, "123")); // min 4
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to long username) DTO")
    void addInvalidDtoWithoutToLongUsername() throws Exception {
        performPostMockMvc(status().isBadRequest(),
                getValuesMapForJson(USERNAME_KEY, "1234567890_123456")); // max 16, is 17
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without username) DTO")
    void addInvalidDtoWithoutUsername() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(USERNAME_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to short name) DTO")
    void addInvalidDtoWithoutToShortName() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(NAME_KEY, "123")); // min 4
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to long name) DTO")
    void addInvalidDtoWithoutToLongName() throws Exception {
        performPostMockMvc(status().isBadRequest(),
                getValuesMapForJson(NAME_KEY, "1234567890_123456")); // max 16, is 17
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without name) DTO")
    void addInvalidDtoWithoutName() throws Exception {
        performPostMockMvc(status().isBadRequest(), getValuesMapForJson(NAME_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Update all-in-once valid fields")
    void updateValidFields() throws Exception {
        var valuesMapForJson = getValuesMapForJson();
        valuesMapForJson.put(ID_KEY, ID.toString());
        valuesMapForJson.put("bio", BIO);
        performPutMockMvc(status().isOk(), valuesMapForJson);
        verify(userService, times(1)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update with ID a single value")
    void updateWithIdASingleValue() throws Exception {
        var valuesMapForJson = new HashMap<String, String>();
        valuesMapForJson.put(ID_KEY, ID.toString());
        valuesMapForJson.put(USERNAME_KEY, USERNAME);
        performPutMockMvc(status().isOk(), valuesMapForJson);
        verify(userService, times(1)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update with Email a single value")
    void updateWithEmailASingleValue() throws Exception {
        var valuesMapForJson = new HashMap<String, String>();
        valuesMapForJson.put(EMAIL_KEY, EMAIL);
        valuesMapForJson.put(USERNAME_KEY, USERNAME);
        performPutMockMvc(status().isOk(), valuesMapForJson);
        verify(userService, times(1)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update fail caused by lack of id & email")
    void updateWithoutEmailAndIdFields() throws Exception {
        var valuesMapForJson = getValuesMapForJson();
        valuesMapForJson.remove(EMAIL_KEY);
        performPutMockMvc(status().isBadRequest(), valuesMapForJson);
        verify(userService, times(0)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update fail caused by to short value")
    void updateFailCausedByToShortValue() throws Exception {
        var valuesMapForJson = getValuesMapForJson();
        valuesMapForJson.put(USERNAME_KEY, "123"); // to short username
        performPutMockMvc(status().isBadRequest(), valuesMapForJson);
        verify(userService, times(0)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update fail caused by to not valid email")
    void updateFailCausedByWrongEmailValue() throws Exception {
        var valuesMapForJson = getValuesMapForJson();
        valuesMapForJson.put(EMAIL_KEY, "invalidEmail");
        performPutMockMvc(status().isBadRequest(), valuesMapForJson);
        verify(userService, times(0)).update(any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Successful delete")
    void deleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Fail delete caused by non existing id")
    void deleteNonExistingId() throws Exception {
        doThrow(UserNotFoundPhotogramException.class).when(userService).delete(any());
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}