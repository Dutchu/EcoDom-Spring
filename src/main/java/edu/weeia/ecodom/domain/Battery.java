package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Battery extends BaseAuditingEntity {
    @ManyToOne
    PhotovoltaicSystem photovoltaicSystemId;
    Float capacity_kWh;
    Float currentCharge_kWh;
}
