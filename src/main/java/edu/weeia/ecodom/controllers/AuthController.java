package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.domain.Authority;
import edu.weeia.ecodom.repositories.AppUserRepository;
import edu.weeia.ecodom.repositories.AuthorityRepository;
import edu.weeia.ecodom.security.jwt.JwtUtils;
import edu.weeia.ecodom.security.model.request.LoginRequest;
import edu.weeia.ecodom.security.model.request.SignupRequest;
import edu.weeia.ecodom.security.model.response.LoginResponse;
import edu.weeia.ecodom.security.model.response.MessageResponse;
import edu.weeia.ecodom.security.model.response.UserInfoResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import edu.weeia.ecodom.services.UserService;
import edu.weeia.ecodom.controllers.util.AuthUtil;
import edu.weeia.ecodom.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;
    AppUserRepository userRepository;
    AuthorityRepository roleRepository;
    PasswordEncoder encoder;
    UserService userService;
    AuthUtil authUtil;

    @PostMapping("/public/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

//      Set the authentication
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        // Collect roles from the UserDetails
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Prepare the response body, now including the JWT token directly in the body
        LoginResponse response = new LoginResponse(userDetails.getUsername(),
                roles, jwtToken);

        // Return the response entity with the JWT token included in the response body
        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.email())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        AppUser user = AppUser.builder()
                .username(signUpRequest.username())
                .email(signUpRequest.email())
                .password(encoder.encode(signUpRequest.password()))
                .build();

        Set<String> strRoles = signUpRequest.getRole();
        Authority role;

        if (strRoles == null || strRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppAuthority.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        } else {
            String roleStr = strRoles.iterator().next();
            if (roleStr.equals("admin")) {
                role = roleRepository.findByRoleName(AppAuthority.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            } else {
                role = roleRepository.findByRoleName(AppAuthority.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            }

//            user.setAccountNonLocked(true);
//            user.setAccountNonExpired(true);
//            user.setCredentialsNonExpired(true);
//            user.setEnabled(true);
//            user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
//            user.setAccountExpiryDate(LocalDate.now().plusYears(1));
//            user.setTwoFactorEnabled(false);
//            user.setSignUpMethod("email");
        }
        user.setAuthorities(Set.of(role));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        AppUser user = userService.findWithAuthoritiesByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user:" + userDetails.getUsername()));

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                roles
        );

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/username")
    public String currentUserName(@AuthenticationPrincipal UserDetails userDetails) {
        return (userDetails != null) ? userDetails.getUsername() : "";
    }

//    @PostMapping("/public/forgot-password")
//    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
//        try {
//            userService.generatePasswordResetToken(email);
//            return ResponseEntity.ok(new MessageResponse("Password reset email sent!"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new MessageResponse("Error sending password reset email"));
//        }
//
//    }

//    @PostMapping("/public/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestParam String token,
//                                           @RequestParam String newPassword) {
//
//        try {
//            userService.resetPassword(token, newPassword);
//            return ResponseEntity.ok(new MessageResponse("Password reset successful"));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(new MessageResponse(e.getMessage()));
//        }
//    }


//    @PostMapping("/disable-2fa")
//    public ResponseEntity<String> disable2FA() {
//        Long userId = authUtil.loggedInUserId();
//        userService.disable2FA(userId);
//        return ResponseEntity.ok("2FA disabled");
//    }


//    @PostMapping("/verify-2fa")
//    public ResponseEntity<String> verify2FA(@RequestParam int code) {
//        Long userId = authUtil.loggedInUserId();
//        boolean isValid = userService.validate2FACode(userId, code);
//        if (isValid) {
//            userService.enable2FA(userId);
//            return ResponseEntity.ok("2FA Verified");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid 2FA Code");
//        }
//    }


//    @GetMapping("/user/2fa-status")
//    public ResponseEntity<?> get2FAStatus() {
//        User user = authUtil.loggedInUser();
//        if (user != null) {
//            return ResponseEntity.ok().body(Map.of("is2faEnabled", user.isTwoFactorEnabled()));
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("User not found");
//        }
//    }


//    @PostMapping("/public/verify-2fa-login")
//    public ResponseEntity<String> verify2FALogin(@RequestParam int code,
//                                                 @RequestParam String jwtToken) {
//        String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
//        User user = userService.findByUsername(username);
//        boolean isValid = userService.validate2FACode(user.getUserId(), code);
//        if (isValid) {
//            return ResponseEntity.ok("2FA Verified");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body("Invalid 2FA Code");
//        }
//    }

}
