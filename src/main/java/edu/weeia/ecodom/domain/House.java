package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
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
    @ManyToOne
    AppUser userId;
    @OneToMany(mappedBy = "houseId")
    Set<Device> devices = new HashSet<>();
    @OneToMany(mappedBy = "houseId")
    Set<PhotovoltaicSystem> systems = new HashSet<>();
    String name;
    String Address;
}
