package edu.weeia.ecodom.security.model.response;

import java.time.LocalDate;
import java.util.List;

public record UserInfoResponse(
        Long id,
        String username,
        String email,
        boolean accountNonLocked,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean enabled,
        List<String>roles
) {
}
