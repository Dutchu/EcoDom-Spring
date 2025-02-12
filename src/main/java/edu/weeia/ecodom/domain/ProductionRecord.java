package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class ProductionRecord extends BaseAuditingEntity {
    @ManyToOne
    PhotovoltaicSystem photovoltaicSystemId;
    LocalDate date;
    Float producedEnergy_kWh;
}
