package edu.weeia.ecodom.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.weeia.ecodom.configuration.Constants;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "APP_USER")
@Builder
@NoArgsConstructor // Replaces your explicit no-args constructor
@AllArgsConstructor // Generates the constructor Lombok's builder needs
public class AppUser extends BaseAuditingEntity {

    //    @Basic(fetch = jakarta.persistence.FetchType.LAZY)
    @Lob
    @JsonIgnore
    byte[] avatar;

    @Size(max = 30)
    private String firstName;

    @Size(max = 30)
    private String lastName;

    @Column(length = 254, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String username; //doesn't need to be unique (multi-tenacy)

    @Column(name = "password_hash", length = 60)
    private String password;

    @JoinTable(
            name = "APP_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "roleName")
    )
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @BatchSize(size = Constants.BATCH_SIZE)
    private Set<Authority> authorities = new HashSet<>();

    @OneToMany(mappedBy = "userId")
    private Set<House> houses = new HashSet<>();

    @Column(nullable = false)
    private boolean active = false;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    private String activationKey;

    private String resetKey;

    public boolean isEnabled() {
        return active;
    }
}