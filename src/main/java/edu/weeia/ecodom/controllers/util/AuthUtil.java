package edu.weeia.ecodom.controllers.util;

import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuthUtil {

    AppUserRepository userRepository;

    public Long loggedInUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user = userRepository.findOneWithAuthoritiesByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    public AppUser loggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findOneWithAuthoritiesByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
