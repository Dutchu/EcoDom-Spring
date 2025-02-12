package edu.weeia.ecodom.security.oauth2;

import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.domain.Authority;
import edu.weeia.ecodom.exceptions.InvalidAuthorityException;
import edu.weeia.ecodom.repositories.AuthorityRepository;
import edu.weeia.ecodom.security.jwt.JwtUtils;
import edu.weeia.ecodom.security.services.UserDetailsImpl;
import edu.weeia.ecodom.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    AuthorityRepository roleRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    String username;
    String idAttributeKey;


    OAuth2LoginSuccessHandler(UserService userService, JwtUtils jwtUtils, AuthorityRepository roleRepository) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()) || "google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();
            if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = attributes.getOrDefault("login", "").toString();
                idAttributeKey = "id";
            } else if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
                username = email.split("@")[0];
                idAttributeKey = "sub";
            } else {
                username = "";
                idAttributeKey = "id";
            }
            System.out.println("HELLO OAUTH: " + email + " : " + name + " : " + username);

            userService.findWithAuthoritiesByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                user.getAuthorities(),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                user.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        AppUser newUser = new AppUser();

                        Set<Authority> userRole =
                                roleRepository.findByRoleNameIn(List.of(AppAuthority.ROLE_USER)); // Fetch existing role
                        newUser.setAuthorities(
                                Optional.of(userRole)
                                        .filter(roles -> !roles.isEmpty())
                                        .orElseThrow(() -> new RuntimeException("Default role not found"))
                        );
                        newUser.setEmail(email);
                        newUser.setUsername(username);
                        userService.register(newUser);
                        DefaultOAuth2User oauthUser = new DefaultOAuth2User(
                                newUser.getAuthorities(),
                                attributes,
                                idAttributeKey
                        );
                        Authentication securityAuth = new OAuth2AuthenticationToken(
                                oauthUser,
                                newUser.getAuthorities(),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()
                        );
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }
        this.setAlwaysUseDefaultTargetUrl(true);

        // JWT TOKEN LOGIC
        DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Extract necessary attributes
        String email = (String) attributes.get("email");
        System.out.println("OAuth2LoginSuccessHandler: " + username + " : " + email);

        Set<Authority> authorities = mapAuthorities(oauth2User);

        AppUser user = userService.findWithAuthoritiesByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));

        authorities.addAll(user.getAuthorities());

        // Create UserDetailsImpl instance
        /***
         *                 null,
         *                 username,
         *                 email,
         *                 null,
         *                 false,
         *                 authorities
         */
        AppUser appUser = AppUser.builder()
                .username(username)
                .email(email)
                .authorities(authorities)
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(appUser);

        // Generate JWT token
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Redirect to the frontend with the JWT token
        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", jwtToken)
                .build().toUriString();
        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /***
     * @param oauth2User
     * @return Mapped out authorities from 0Auth provider to system enum AppAuthority or empty
     */
    private Set<Authority> mapAuthorities(DefaultOAuth2User oauth2User) {
        Set<Authority> result;
        result = oauth2User.getAuthorities().stream()
                .map(grantedAuthority -> {
                    try {
                        return Authority.fromString(grantedAuthority.getAuthority());
                    } catch (InvalidAuthorityException e) {
                        throw new RuntimeException(e);
                    }
                })
                .flatMap(Optional::stream) // This will drop any empty Optionals.
                .collect(Collectors.toSet());

        return result;
    }
}
