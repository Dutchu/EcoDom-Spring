package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.AppAuthority;
import edu.weeia.ecodom.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {
    @Transactional(readOnly = true)
    Set<Authority> findByRoleNameIn(Collection<AppAuthority> authorities);
    Optional<Authority> findByRoleName(AppAuthority authority);
}
