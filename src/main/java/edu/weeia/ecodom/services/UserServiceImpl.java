package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.mapper.UserMapper;
import edu.weeia.ecodom.api.v1.model.UserPreviewDto;
import edu.weeia.ecodom.api.v1.model.UserCreatedDto;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final AuthorityService authorityService;
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final FileService fileService;

    @Override
    public UserCreatedDto register(AppUser user) {
        if (user.getPassword() != null)
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        return UserMapper.INSTANCE.mapAfterCreate(userRepository.save(user));
    }

    @Override
    public boolean activateUsers(List<Long> selectedUsers) {
        userRepository.findAllById(selectedUsers).stream().map(this::setUserStatusActive).forEach(userRepository::save);
        return true;
    }

    private AppUser setUserStatusActive(AppUser user) {
        user.setActive(true);
        return user;
    }


    public Optional<AppUser> findWithAuthoritiesByEmail(String email) {
        return userRepository.findOneWithAuthoritiesByEmail(email);

    }

    @Override
    public AppUser findWithAuthoritiesByUsername(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find username: " + username));
    }

    @Override
    public Page<UserPreviewDto> getAllToReview(Pageable pageable) {
        return userRepository.findAllByActiveIsFalse(pageable)
                .map(UserMapper.INSTANCE::mapForPreview);
    }

//    public UserCreatedDTO save(UserSignUpHashedDTO toCreate) {
//
//        // Create a new user
//        AppUser user = new AppUser();
//        user.setEmail(toCreate.email());
//        user.setPassword(toCreate.hashedPassword());
//        user.setFirstName(toCreate.firstName());
//        user.setLastName(toCreate.lastName());
//        user.setUsername(toCreate.username());
//
//        // Set user role
//        // user.setRole("USER");
//        //TODO: Admin logic
//        user.setAuthorities(authorityService.findByNameIn("ROLE_USER"));
//
//        try {
//            user.setAvatar(fileService.getDefaultImage(AppUser.class));
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        AppUser savedUser = userRepository.save(user);
//
//        return new UserCreatedDTO(
//                savedUser.getId(),
//                savedUser.getUsername(),
//                savedUser.getEmail(),
//                savedUser.getAuthorities()
//        );
//      }

//    @Override
//    public Long getFacilityId(String username) {
//        AppUser appUser = userRepository.findOneByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User " + username + "was not found!"));
//        Set<BreedingFacility> breedingFacilities = appUser.getBreedingFacilities();
//        return null;
//    }
//
//    public void saveAsAdmin(UserSignUpDto user) {
//        // Create a new Admin
//    }
}