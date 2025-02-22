package edu.weeia.ecodom.security;

import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.domain.Authority;
import edu.weeia.ecodom.repositories.AppUserRepository;
import edu.weeia.ecodom.repositories.AuthorityRepository;
import edu.weeia.ecodom.security.jwt.AuthEntryPointJwt;
import edu.weeia.ecodom.security.jwt.AuthTokenFilter;
import edu.weeia.ecodom.security.jwt.JwtUtils;
import edu.weeia.ecodom.security.oauth2.AuthMapper;
import edu.weeia.ecodom.security.oauth2.OAuth2LoginSuccessHandler;
import edu.weeia.ecodom.security.services.UserDetailsServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
@Configuration
@Profile({"dev", "default"})
public class DevSecurityConfig {


    private final OAuth2LoginSuccessHandler successHandler;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        return new AuthTokenFilter(jwtUtils, userDetailsService);
    }

    DevSecurityConfig(@Lazy OAuth2LoginSuccessHandler successHandler,
                      @Lazy UserDetailsServiceImpl userDetailsService,
                      AuthEntryPointJwt unauthorizedHandler,
                      JwtUtils jwtUtils) {
        this.successHandler = successHandler;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;

        this.userDetailsService = userDetailsService;
    }

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/v2/api-docs",
            "/h2-console/**",
            "/webjars/**",
            "/static/**",
            "/images/**",          // <-- Add this line to whitelist images
            "/css/**",             // <-- Add this line to whitelist CSS files if needed
            "/js/**",              // <-- Add this line to whitelist JS files if needed
            "/", //landing page is allowed for all
            "/landing",
            "/signup",
            "/favicon.ico",
            "/dog/**",
            "/error/**",
            "/index",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/h2-console/**",
            "/ws/**",
            "/app/**",
            "/topic/**",
            "/api/auth/public/**",

    };

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http, AuthMapper authMapper) throws Exception {
        http.csrf(csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/h2-console/**",
                                "/ws/**",
                                "/app/**",
                                "/topic/**",
                                "/api/auth/public/**"
                        )
                        .ignoringRequestMatchers("/ws/**", "/app/**", "/topic/**")
                        .ignoringRequestMatchers("/api/auth/public/**"));
        http
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(ah -> ah
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers("/api/admin/**").hasAuthority(AppAuthority.ROLE_ADMIN.name())
                        .requestMatchers("/api/csrf-token").permitAll()
                        .requestMatchers("/api/auth/public/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(authMapper))// Use custom mapper
                        .successHandler(successHandler)); // Your existing handler

        http.exceptionHandling(exception
                -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.addFilterBefore(authenticationJwtTokenFilter(jwtUtils, userDetailsService),
                UsernamePasswordAuthenticationFilter.class);
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public CommandLineRunner initUserData(AuthorityRepository roleRepository,
                                          AppUserRepository userRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            Authority userAuthority = roleRepository.findByRoleName(AppAuthority.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Authority(AppAuthority.ROLE_USER)));

            Authority adminAuthority = roleRepository.findByRoleName(AppAuthority.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Authority(AppAuthority.ROLE_ADMIN)));

            if (!userRepository.existsByUsername("user1")) {
                AppUser user1 = AppUser.builder()
                        .username("user1")
                        .email("user1@example.com")
                        .password(passwordEncoder.encode("password1"))
                        .authorities(Set.of(userAuthority))
                        .build();
                userRepository.save(user1);
            }

            if (!userRepository.existsByUsername("admin")) {
                AppUser admin = AppUser.builder()
                        .username("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("adminPass"))
                        .authorities(Set.of(adminAuthority))
                        .build();
                userRepository.save(admin);
            }
        };
    }

    //TODO: IMPORTANT TO UNDERSTAND HOW THIS WORK
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/error/403");
        return accessDeniedHandler;
    }
}