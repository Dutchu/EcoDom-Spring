package edu.weeia.ecodom.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/***
 * userId: References the user who owns this house.
 * address: Physical address of the house/apartment.
 * name: A friendly name or label (e.g. “Moje Mieszkanie”, “Dom Letniskowy”).
 * A house can have:
 * Multiple devices (DEVICE).
 * Optionally a photovoltaic system (PHOTOVOLTAIC_SYSTEM).
 * A selected tariff (you might store a tariffId in HOUSE or manage a many-to-many relation if houses can switch tariffs historically).
 */
@Entity
@Getter
@Setter
public class House extends BaseAuditingEntity {
    @JsonBackReference
    @ManyToOne
    AppUser appUser;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "house")
    Set<Device> devices = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "house")
    Set<PhotovoltaicSystem> systems = new HashSet<>();
    String name;
    String address;
}
