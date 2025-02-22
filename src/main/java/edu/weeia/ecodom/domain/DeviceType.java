package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
/***
 *     FRIDGE(800),
 *     OVEN(4000),
 *     WASHING_MACHINE(1400),
 *     TV(80),
 *     LIGHTS(100),
 *     PC(400);
 */
public class DeviceType extends BaseAuditingEntity {
    @Enumerated(EnumType.STRING)
    private Devices name;
    private Integer wattage;
}