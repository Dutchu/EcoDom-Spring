package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class PhotovoltaicSystem extends BaseAuditingEntity {

    @ManyToOne
    House houseId;
    @OneToMany(mappedBy = "photovoltaicSystemId")
    Set<Battery> batteries = new HashSet<>();
    @OneToMany(mappedBy = "photovoltaicSystemId")
    Set<ProductionRecord> records = new HashSet<>();
    Float panelArea_m2;
    Float maxPower_kW;
}
