package pl.tscript3r.photogram.api.v1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
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
import pl.tscript3r.photogram.infrastructure.exception.NotFoundPhotogramException;
import pl.tscript3r.photogram.user.UserService;
import pl.tscript3r.photogram.user.api.v1.UserController;
import pl.tscript3r.photogram.user.api.v1.UserDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.api.v1.dtos.UserDtoTest.getDefaultUserDto;
import static pl.tscript3r.photogram.infrastructure.MappingsConsts.EMAIL_PARAM;
import static pl.tscript3r.photogram.infrastructure.MappingsConsts.USER_MAPPING;
import static pl.tscript3r.photogram.user.api.v1.UserController.*;

@DisplayName("Users controller")
@WebMvcTest(UserController.class)
@WithMockUser("spring")
class UserControllerTest {

    private static final String EMAIL_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final String USERNAME_KEY = "username";
    private static final String FIRSTNAME_KEY = "firstname";
    private static final String ID_KEY = "id";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

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
        assertEquals(expected.getFirstname(), actual.getFirstname());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertNull(actual.getPassword());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getBio(), actual.getBio());
    }

    @Test
    @DisplayName("Get by ID not found")
    void getByIdNotFound() throws Exception {
        when(userService.getByIdDto(any())).thenThrow(NotFoundPhotogramException.class);
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
        performPostMockMvc(false, status().isCreated(), getValuesMapFromJson());
        verify(userService, times(1)).save(any(UserDto.class));
    }

    private void performPostMockMvc(Boolean includeId, ResultMatcher matcher, Map<String, String> valuesMap) throws Exception {
        String mapping = USER_MAPPING;
        if (includeId)
            mapping += "/1";
        performMockMvc(MockMvcRequestBuilders.post(mapping), matcher, valuesMap);
    }

    private void performPutMockMvc(Boolean includeId, ResultMatcher matcher, Map<String, String> valuesMap) throws Exception {
        String mapping = USER_MAPPING;
        if (includeId)
            mapping += "/1";
        performMockMvc(MockMvcRequestBuilders.put(mapping), matcher, valuesMap);
    }

    private void performMockMvc(MockHttpServletRequestBuilder mockHttpServletRequestBuilder, ResultMatcher matcher,
                                Map<String, String> valuesMap) throws Exception {
        mockMvc.perform(mockHttpServletRequestBuilder
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(valuesMap)))
                .andExpect(matcher);
    }

    private Map<String, String> getValuesMapFromJson() {
        Map<String, String> values = new HashMap<>();
        values.put(USERNAME_KEY, USERNAME);
        values.put(PASSWORD_KEY, PASSWORD);
        values.put(FIRSTNAME_KEY, NAME);
        values.put(EMAIL_KEY, EMAIL);
        values.put("bio", BIO);
        return values;
    }

    @Test
    @DisplayName("Add invalid (to short password) DTO")
    void addInvalidPasswordShortDto() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(PASSWORD_KEY, "12345")); // min 6
        verify(userService, times(0)).save(any(UserDto.class));
    }

    private Map<String, String> getValuesMapFromJson(String editedKey, String newValue) {
        var result = getValuesMapFromJson();
        result.put(editedKey, newValue);
        return result;
    }

    @Test
    @DisplayName("Add invalid (to long password) DTO")
    void addInvalidPasswordLongDto() throws Exception {
        performPostMockMvc(false, status().isBadRequest(),
                getValuesMapFromJson(PASSWORD_KEY, "1234567890_1234567890_1234567890_")); // max 32, is 33
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without password) DTO")
    void addInvalidDtoWithoutPassword() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(PASSWORD_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    private Map<String, String> getValuesMapFromJson(String withoutKey) {
        var result = getValuesMapFromJson();
        result.remove(withoutKey);
        return result;
    }

    @Test
    @DisplayName("Add invalid (email) DTO")
    void addInvalidEmailDto() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(EMAIL_KEY, "invalidEmail.com"));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without email) DTO")
    void addInvalidDtoWithoutEmail() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(EMAIL_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to short username) DTO")
    void addInvalidDtoWithoutToShortUsername() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(USERNAME_KEY, "123")); // min 4
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to long username) DTO")
    void addInvalidDtoWithoutToLongUsername() throws Exception {
        performPostMockMvc(false, status().isBadRequest(),
                getValuesMapFromJson(USERNAME_KEY, "1234567890_123456")); // max 16, is 17
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without username) DTO")
    void addInvalidDtoWithoutUsername() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(USERNAME_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to short firstname) DTO")
    void addInvalidDtoWithoutToShortName() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(FIRSTNAME_KEY, "123")); // min 4
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (to long firstname) DTO")
    void addInvalidDtoWithoutToLongName() throws Exception {
        performPostMockMvc(false, status().isBadRequest(),
                getValuesMapFromJson(FIRSTNAME_KEY, "1234567890_123456")); // max 16, is 17
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Add invalid (without firstname) DTO")
    void addInvalidDtoWithoutName() throws Exception {
        performPostMockMvc(false, status().isBadRequest(), getValuesMapFromJson(FIRSTNAME_KEY));
        verify(userService, times(0)).save(any(UserDto.class));
    }

    @Test
    @DisplayName("Update all-in-once valid fields")
    void updateValidFields() throws Exception {
        var valuesMapForJson = getValuesMapFromJson();
        valuesMapForJson.put(ID_KEY, ID.toString());
        valuesMapForJson.put("bio", BIO);
        performPutMockMvc(true, status().isOk(), valuesMapForJson);
        verify(userService, times(1)).update(any(), any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update with ID a single value")
    void updateWithIdASingleValue() throws Exception {
        var valuesMapForJson = new HashMap<String, String>();
        valuesMapForJson.put(ID_KEY, ID.toString());
        valuesMapForJson.put(USERNAME_KEY, USERNAME);
        performPutMockMvc(true, status().isOk(), valuesMapForJson);
        verify(userService, times(1)).update(any(), any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update fail caused by to short value")
    void updateFailCausedByToShortValue() throws Exception {
        var valuesMapForJson = getValuesMapFromJson();
        valuesMapForJson.put(USERNAME_KEY, "123"); // to short username
        performPutMockMvc(true, status().isBadRequest(), valuesMapForJson);
        verify(userService, times(0)).update(any(), any(), any(UserDto.class));
    }

    @Test
    @DisplayName("Update fail caused by to not valid email")
    void updateFailCausedByWrongEmailValue() throws Exception {
        var valuesMapForJson = getValuesMapFromJson();
        valuesMapForJson.put(EMAIL_KEY, "invalidEmail");
        performPutMockMvc(true, status().isBadRequest(), valuesMapForJson);
        verify(userService, times(0)).update(any(), any(), any(UserDto.class));
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
        doThrow(NotFoundPhotogramException.class).when(userService).delete(any(), any());
        mockMvc.perform(MockMvcRequestBuilders.delete(USER_MAPPING + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get avatar")
    void getAvatar() throws Exception {
        when(userService.getAvatar(any())).thenReturn(IMAGE_RESPONSE_ENTITY);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/" + ID + AVATAR_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Upload avatar")
    void postAvatar() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(USER_MAPPING + "/" + ID + AVATAR_MAPPING)
                .file(IMAGE_MOCK_MULTIPART_FILE)
                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
        verify(userService, times(1)).saveAvatar(any(), any(), any());
    }

    @Test
    @DisplayName("Email token confirmation")
    void emailTokenConfirmation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_MAPPING + EMAIL_CONFIRMATION_MAPPING + "?" +
                TOKEN_PARAM + "=" + RandomStringUtils.randomNumeric(32))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).confirmEmail(any());
    }

    @Test
    @DisplayName("Password reset")
    void passwordReset() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_MAPPING + PASSWORD_RESET_MAPPING + "?" +
                EMAIL_PARAM + "=" + EMAIL)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).resetPassword(any());
    }

    @Test
    @DisplayName("Get followers")
    void getFollowers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/" + ID + FOLLOWERS_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(userService, times(1)).getFollowers(any(), any());
    }

    @Test
    @DisplayName("Get follows")
    void getFollows() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/" + ID + FOLLOWS_MAPPING)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        verify(userService, times(1)).getFollows(any(), any());
    }

    @Test
    @DisplayName("Follow")
    void follow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_MAPPING + FOLLOW_MAPPING + "/" + ID))
                .andExpect(status().isOk());

        verify(userService, times(1)).follow(any(), any());
    }

    @Test
    @DisplayName("Unfollow")
    void unfollow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put(USER_MAPPING + UNFOLLOW_MAPPING + "/" + ID))
                .andExpect(status().isOk());

        verify(userService, times(1)).unfollow(any(), any());
    }

}