package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.security.services.UserDetailsImpl;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContext;

import java.util.Optional;

@Component
public class AuditingService implements AuditorAware<AppUser> {
    @Override
    public Optional<AppUser> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(principal -> principal instanceof UserDetailsImpl ? ((UserDetailsImpl) principal).getAppUser() : null);
    }
}
