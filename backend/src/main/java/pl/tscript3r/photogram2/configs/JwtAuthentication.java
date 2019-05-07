package pl.tscript3r.photogram2.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.tscript3r.photogram2.api.v1.dtos.LoginUserDto;
import pl.tscript3r.photogram2.exceptions.PhotogramException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtAuthentication extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    JwtAuthentication(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        LoginUserDto loginUserDto;
        try {
            loginUserDto = objectMapper.readValue(request.getInputStream(), LoginUserDto.class);
        } catch (Exception e) {
            throw new PhotogramException("Unable to convert Json into Java Object: " + e, e);
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(),
                loginUserDto.getPassword()));
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        List<String> roles = new ArrayList<>();
        user.getAuthorities()
                .forEach(authority ->
                        roles.add(authority.getAuthority()));

        String jwtToken = JWT.create()
                .withIssuer("Photogram")
                .withSubject(user.getUsername())
                .withArrayClaim("roles", roles.stream().toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SecurityConstants.SECRET));
        response.addHeader(SecurityConstants.HEADER_TYPE, SecurityConstants.TOKEN_PREFIX + jwtToken);
    }

}

