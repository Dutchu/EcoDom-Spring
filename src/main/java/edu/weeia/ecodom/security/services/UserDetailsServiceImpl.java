package edu.weeia.ecodom.security.services;

import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) {
        return new UserDetailsImpl(userService.findWithAuthoritiesByUsername(username));

//                .orElseThrow(() -> new UsernameNotFoundException(" User with login:" + username + " was not found in the " + " database "));

//        App result = userFromDatabase
//                .map(() -> getCustomUserDetails())
//                .orElseThrow(() -> new UsernameNotFoundException(" User with login:" + username + " was not found in the " + " database "));
//        System.out.println(result);
//        return new AppUserDetails(result);
    }

//    @Transactional(readOnly = true)
//    public AppUserDetails getCustomUserDetails(AppUser user) {
//
//        return new AppUserDetails(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getAuthorities(),
//                user.isEnabled(), user.isAccountNonExpired(), user.isCredentialsNonExpired(), user.isAccountNonLocked());
//    }

}