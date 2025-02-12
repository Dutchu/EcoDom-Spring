package edu.weeia.ecodom.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class AuthMapper implements GrantedAuthoritiesMapper {
    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(
            Collection<? extends GrantedAuthority> authorities) {

        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

        // Always grant ROLE_USER to OAuth2 users
        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        // Optionally grant ROLE_ADMIN based on conditions (e.g., email)
        authorities.stream()
                .filter(auth -> auth instanceof OAuth2UserAuthority)
                .map(auth -> (OAuth2UserAuthority) auth)
                .forEach(oauthAuth -> {
                    String email = (String) oauthAuth.getAttributes().get("email");
                    if (email != null && email.endsWith("@admin.com")) { // Example condition
                        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                });

        return mappedAuthorities;
    }
}
