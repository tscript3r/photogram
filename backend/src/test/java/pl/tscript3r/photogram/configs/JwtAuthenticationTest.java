package pl.tscript3r.photogram.configs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import pl.tscript3r.photogram.exceptions.PhotogramException;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayName("JWT authentication")
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationTest {

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    JwtAuthentication jwtAuthentication;

    @Test
    @DisplayName("Attempt authentication")
    void attemptAuthentication() {
        var request = new MockHttpServletRequest("GET", "/");
        var response = mock(HttpServletResponse.class);
        request.setContent("{\"username\":\"test\",\"password\":\"test_password\"}".getBytes());
        var result = jwtAuthentication.attemptAuthentication(request, response);
        verify(authenticationManager, times(1)).authenticate(any());
    }

    @Test
    @DisplayName("Attempt authentication JSON mapping fail")
    void attemptAuthenticationJsonFail() {
        var request = new MockHttpServletRequest("GET", "/");
        var response = mock(HttpServletResponse.class);
        request.setContent("{\"username:\"test\",\"password\":\"test_password\"".getBytes());
        assertThrows(PhotogramException.class, () -> jwtAuthentication.attemptAuthentication(request, response));
        verify(authenticationManager, times(0)).authenticate(any());
    }

}