package pl.tscript3r.photogram2.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.tscript3r.photogram2.domains.Role;
import pl.tscript3r.photogram2.domains.User;
import pl.tscript3r.photogram2.services.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Primary
@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        User user = userService.getByUsername(username);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        Set<Role> userRoles = user.getRoles();
        userRoles.forEach(userRole ->
                authorities.add(new SimpleGrantedAuthority(userRole.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

}