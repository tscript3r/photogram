package pl.tscript3r.photogram.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.tscript3r.photogram.infrastructure.mapper.MapperService;
import pl.tscript3r.photogram.post.image.ImageService;
import pl.tscript3r.photogram.user.UserRepository;
import pl.tscript3r.photogram.user.UserService;
import pl.tscript3r.photogram.user.api.v1.UserDto;
import pl.tscript3r.photogram.user.email.EmailConfirmationRepository;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.tscript3r.photogram.Consts.*;
import static pl.tscript3r.photogram.api.v1.mappers.UserMapperTest.compareUserDtoWithUser;
import static pl.tscript3r.photogram.infrastructure.MappingsConsts.*;
import static pl.tscript3r.photogram.user.api.v1.UserController.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserIT {

    static final String AUTHORIZATION = "Authorization";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MapperService mapperService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    @Autowired
    EmailConfirmationRepository emailConfirmationRepository;

    @MockBean
    ExecutorService senderExecutorService;

    @MockBean
    ImageService imageService;

    private Long addedUserId;

    @BeforeEach
    void setUp() {
        addedUserId = null;
    }

    @AfterEach
    void cleanUp() {
        if(addedUserId != null && userRepository.existsById(addedUserId))
            userRepository.deleteById(addedUserId);
    }

    @Test
    @DisplayName("Register new user")
    void registerNewUser() throws Exception {
        var userDto = registerUserPostRequest();
        assertTrue(userRepository.existsById(userDto.getId()));
        assertFalse(userDto.getEmailConfirmed());
        assertEquals(FIRSTNAME,userDto.getFirstname());
        assertEquals(USERNAME, userDto.getUsername());
        assertEquals(EMAIL, userDto.getEmail());
        assertEquals(BIO,userDto.getBio());

        verify(senderExecutorService, times(1)).execute(any()); // confirmation mail send
    }

    UserDto registerUserPostRequest() throws Exception {
        var userDto = jsonToUserDto(mockMvc.perform(post(USER_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"firstname\": \"" + FIRSTNAME + "\",\n" +
                        "\t\"username\": \"" + USERNAME + "\",\n" +
                        "\t\"password\": \"" + PASSWORD + "\",\n" +
                        "\t\"email\": \"" + EMAIL + "\",\n" +
                        "\t\"bio\": \"" + BIO + "\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString());
        addedUserId = userDto.getId();
        return userDto;
    }

    private UserDto jsonToUserDto(String json) throws IOException {
        return objectMapper.readValue(json, UserDto.class);
    }

    @Test
    @DisplayName("Login as new user without email confirmation")
    void loginAsNewUserWithoutEmailConfirmation() throws Exception {
        registerUserPostRequest();
        mockMvc.perform(post(LOGIN_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"username\": \"" + USERNAME + "\",\n" +
                        "\t\"password\": \"" + PASSWORD + "\"\n" +
                        "}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Email confirmation")
    void confirmEmailByToken() throws Exception {
        registerUserPostRequest();
        var emailConfirmation = emailConfirmationRepository.findByUser(userRepository.findById(addedUserId).get()).get();
        assertFalse(emailConfirmation.getConfirmed());
        var token = emailConfirmation.getToken().toString().replace("-", "");
        mockMvc.perform(put(USER_MAPPING + "/" + EMAIL_CONFIRMATION_MAPPING + "?" +
                TOKEN_PARAM + "=" + token)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        emailConfirmation = emailConfirmationRepository.findByUser(userRepository.findById(addedUserId).get()).get();
        assertTrue(emailConfirmation.getConfirmed());
    }

    @Test
    @DisplayName("Login as new user with email confirmed")
    void loginAsNewUserWithEmailConfirmed() throws Exception {
        registerUserPostRequest();
        setEmailConfirmedOnNewUser(USERNAME);

        mockMvc.perform(get(USER_MAPPING)
                .header(AUTHORIZATION, loginAsNewUser(USERNAME, PASSWORD)))
                .andExpect(status().isOk());
    }

    private String loginAsNewUser(String username, String password) throws Exception {
        var mvcResult = mockMvc.perform(post(LOGIN_MAPPING)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"username\": \"" + username + "\",\n" +
                        "\t\"password\": \"" + password + "\"\n" +
                        "}"))
                .andExpect(status().isOk())
                .andReturn();
        return mvcResult.getResponse().getHeader(AUTHORIZATION);
    }

    private void setEmailConfirmedOnNewUser(String username) {
        var emailConfirmation = emailConfirmationRepository
                .findByUser(userRepository.findByUsername(username).get()).get();
        emailConfirmation.setConfirmed(true);
        emailConfirmationRepository.save(emailConfirmation);
    }

    @Test
    @DisplayName("Get by id")
    void getUserById() throws Exception {
        var userDto = jsonToUserDto(mockMvc.perform(get(USER_MAPPING + "/" + ID)
                    .contentType(MediaType.APPLICATION_JSON_UTF8))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString());
        assertEquals(ID, userDto.getId());
    }

    @Test
    @DisplayName("Get by username")
    void getByUsername() throws Exception {
        registerUserPostRequest();
        setEmailConfirmedOnNewUser(USERNAME);
        var userDto = jsonToUserDto(mockMvc.perform(get(USER_MAPPING + "/" + FIND_MAPPING + "?" +
                USERNAME_PARAM + "=" + USERNAME)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertEquals(USERNAME, userDto.getUsername());
    }

    @Test
    @DisplayName("Get by email")
    void getByEmail() throws Exception {
        registerUserPostRequest();
        setEmailConfirmedOnNewUser(USERNAME);
        var userDto = jsonToUserDto(mockMvc.perform(get(USER_MAPPING + "/" + FIND_MAPPING + "?" +
                EMAIL_PARAM + "=" + EMAIL)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertEquals(EMAIL, userDto.getEmail());
    }

    @Test
    @DisplayName("Get by id (second way)")
    void getById() throws Exception {
        registerUserPostRequest();
        setEmailConfirmedOnNewUser(USERNAME);
        var userDto = jsonToUserDto(mockMvc.perform(get(USER_MAPPING + "/" + FIND_MAPPING + "?" +
                ID_PARAM + "=" + ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString());
        assertEquals(ID, userDto.getId());
    }

    @Test
    @DisplayName("Update all (possible) values at once")
    void updateAllValues() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();
        var userDto = jsonToUserDto(mockMvc.perform(put(USER_MAPPING + "/" + addedUserId)
                .header(AUTHORIZATION, bearer)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"firstname\": \"" + SECOND_FIRSTNAME + "\",\n" +
                        "\t\"username\": \"" + SECOND_USERNAME + "\",\n" +
                        "\t\"password\": \"" + SECOND_PASSWORD + "\",\n" +
                        "\t\"email\": \"" + SECOND_EMAIL + "\",\n" +
                        "\t\"bio\": \"" + SECOND_BIO + "\"\n" +
                        "}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertEquals(SECOND_FIRSTNAME, userDto.getFirstname());
        assertEquals(SECOND_USERNAME, userDto.getUsername());
        assertEquals(SECOND_EMAIL, userDto.getEmail());
        assertEquals(SECOND_BIO, userDto.getBio());
        assertFalse(userDto.getEmailConfirmed());

        setEmailConfirmedOnNewUser(SECOND_USERNAME);
        assertNotNull(loginAsNewUser(SECOND_USERNAME, SECOND_PASSWORD));

        compareUserDtoWithUser(userDto, userRepository.findById(userDto.getId()).get());
    }

    private String registerAndConfirmEmailAndLogin() throws Exception {
        registerUserPostRequest();
        setEmailConfirmedOnNewUser(USERNAME);
        return loginAsNewUser(USERNAME, PASSWORD);
    }



    @Test
    @DisplayName("Update firstname and bio")
    void updateFirstnameAndBio() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();
        var userDto = jsonToUserDto(mockMvc.perform(put(USER_MAPPING + "/" + addedUserId)
                .header(AUTHORIZATION, bearer)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"firstname\": \"" + SECOND_FIRSTNAME + "\",\n" +
                        "\t\"bio\": \"" + SECOND_BIO + "\"\n" +
                        "}"))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString());
        assertEquals(SECOND_FIRSTNAME, userDto.getFirstname());
        assertEquals(SECOND_BIO, userDto.getBio());

        compareUserDtoWithUser(userDto, userRepository.findById(userDto.getId()).get());
    }

    @Test
    @DisplayName("Update other users data without access to it")
    void updateOtherUsersDataWithoutAccess() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();
        mockMvc.perform(put(USER_MAPPING + "/" + ID)
                .header(AUTHORIZATION, bearer)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"bio\": \"" + SECOND_BIO + "\"\n" +
                        "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update without login")
    void updateWithoutLogin() throws Exception {
        mockMvc.perform(put(USER_MAPPING + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\n" +
                        "\t\"bio\": \"" + SECOND_BIO + "\"\n" +
                        "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete own account")
    void deleteOwnAccount() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();
        mockMvc.perform(delete(USER_MAPPING + "/" + addedUserId)
                .header(AUTHORIZATION, bearer))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Delete other users account (without permission)")
    void deleteOtherUsersAccount() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();
        mockMvc.perform(delete(USER_MAPPING + "/" + ID)
                .header(AUTHORIZATION, bearer))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get avatar from new user")
    void getAvatar() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();

         mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/" + addedUserId + AVATAR_MAPPING)
                .header(AUTHORIZATION, bearer)
                .accept(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).getAvatar(any());
    }

    @Test
    @DisplayName("Get avatar from other user")
    void getAvatarFromOtherUser() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();

        mockMvc.perform(MockMvcRequestBuilders.get(USER_MAPPING + "/" + ID + AVATAR_MAPPING)
                .header(AUTHORIZATION, bearer)
                .accept(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).getAvatar(any());
    }

    @Test
    @DisplayName("Upload own avatar")
    void uploadOwnAvatar() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();

        mockMvc.perform(MockMvcRequestBuilders.multipart(USER_MAPPING + "/" + addedUserId + AVATAR_MAPPING)
                .file(IMAGE_MOCK_MULTIPART_FILE)
                .header(AUTHORIZATION, bearer)
                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

        verify(imageService, times(1)).saveAvatar(any(), any());
    }

    @Test
    @DisplayName("Upload avatar for other user")
    void uploadOtherUsersAvatar() throws Exception {
        var bearer = registerAndConfirmEmailAndLogin();

        mockMvc.perform(MockMvcRequestBuilders.multipart(USER_MAPPING + "/" + ID + AVATAR_MAPPING)
                .file(IMAGE_MOCK_MULTIPART_FILE)
                .header(AUTHORIZATION, bearer)
                .accept(MediaType.MULTIPART_FORM_DATA)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());

        verify(imageService, times(0)).saveAvatar(any(), any());
    }

    @Test
    @DisplayName("Confirm email with token")
    void confirmEmail() throws Exception {
        registerNewUser();
        var emailConfirmation = emailConfirmationRepository.findByUser(userRepository.findById(addedUserId).get()).get();
        mockMvc.perform(MockMvcRequestBuilders.put(USER_MAPPING + "/" + EMAIL_CONFIRMATION_MAPPING + "?" +
                TOKEN_PARAM + "=" + emailConfirmation.getToken().toString().replace("-", "").toUpperCase())
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}

