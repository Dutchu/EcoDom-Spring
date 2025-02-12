package edu.weeia.ecodom.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.domain.Authority;
import edu.weeia.ecodom.exceptions.InvalidAuthorityException;
import edu.weeia.ecodom.services.AuthorityService;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import edu.weeia.ecodom.configuration.Constants;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    @Getter
    private final AppUser appUser;

    public UserDetailsImpl(AppUser appUser) {
        this.appUser = appUser;
    }

    public String getUsername() {
        return appUser.getUsername();
    }

    public String getPassword() {
        return this.appUser.getPassword();
    }

    public String getEmail() {
        return this.appUser.getEmail();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.appUser.getAuthorities();
    }

    public void setUsername(String username) {
        this.appUser.setUsername(username);
    }

    public void setEmail(String email) {
        this.appUser.setEmail(email);
    }

    public void setPassword(String password) {
        this.appUser.setPassword(password);
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.appUser.setAuthorities(new HashSet<>(authorities));
    }

    private boolean isValidAppAuthority(String authority) {
        try {
            AppAuthority.valueOf(authority);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @JsonIgnore
    public boolean isUser() {
        return getGrantedAuthorities().contains(AppAuthority.ROLE_USER.name());
    }

    @JsonIgnore
    public boolean isSystemAdmin() {
        return getGrantedAuthorities().contains(AppAuthority.ROLE_ADMIN.name());
    }

    public Collection<String> getGrantedAuthorities() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(username, that.username) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(authorities, that.authorities) && Objects.equals(appUser, that.appUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}