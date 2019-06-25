package pl.tscript3r.photogram.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static pl.tscript3r.photogram.api.v1.controllers.MappingsConsts.POST_MAPPING;
import static pl.tscript3r.photogram.api.v1.controllers.MappingsConsts.USER_MAPPING;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] PUBLIC_MATCHERS = {USER_MAPPING + "/login", USER_MAPPING + "/**", POST_MAPPING + "/**", "/image/**",
            "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/h2-console/**"};

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var jwtAuthentication = new JwtAuthentication(authenticationManager());
        jwtAuthentication.setFilterProcessesUrl(PUBLIC_MATCHERS[0]);
        // TODO: for h2-console
        http.headers().frameOptions().disable();
        http.csrf().disable().cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthentication)
                .addFilterBefore(new JwtAuthorization(), UsernamePasswordAuthenticationFilter.class);
    }

}
