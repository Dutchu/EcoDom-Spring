package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.Authority;
import edu.weeia.ecodom.repositories.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    public void save(Authority auth) {
        authorityRepository.save(auth);
    }

    public Set<Authority> findByNameIn(AppAuthority... roles) {
        return authorityRepository.findByRoleNameIn(Set.of(roles));
    }
}
