package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.model.UserPreviewDto;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.api.v1.model.UserCreatedDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<AppUser> findWithAuthoritiesByEmail(String email);
    UserCreatedDto register(AppUser user);
//    UserCreatedDto save(UserSignUpHashedDTO user);
//    Long getFacilityId(String username);
    AppUser findWithAuthoritiesByUsername(String username);
    Page<UserPreviewDto> getAllToReview(Pageable pageable);
    boolean activateUsers(List<Long> selectedUsers);
}
