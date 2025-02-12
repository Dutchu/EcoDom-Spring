package edu.weeia.ecodom.domain;

import edu.weeia.ecodom.exceptions.InvalidAuthorityException;
import jakarta.persistence.*;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Setter
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    private AppAuthority roleName;

    public Authority(){}

    public Authority(AppAuthority authority)  {
        this.roleName = authority;
    }

    // Factory method that returns an Optional
    public static Optional<Authority> fromString(String authName) throws InvalidAuthorityException {
        try {
            AppAuthority appAuthority = AppAuthority.valueOf(authName);
            return Optional.of(new Authority(appAuthority));
        } catch (IllegalArgumentException e) {
            // The string did not match any enum constant.
            throw new InvalidAuthorityException(Collections.singleton(authName));
        }
    }

    @Override
    public String getAuthority() {
        return roleName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Authority authority1 = (Authority) o;

        return roleName.equals(authority1.roleName);
    }
    @Override
    public int hashCode()
    {
        return roleName.hashCode();
    }
    @Override
    public String toString() {
        return """
            Authority{\
            authority='\
            """ + roleName + '\'' +
                '}';
    }
}
