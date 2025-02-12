package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/***
 * name: Label (e.g. “Standard Tariff G11”, “Weekend Tariff”).
 * dayRate_kWh: Cost per kWh during the day.
 * nightRate_kWh: Cost per kWh during the night.
 * dayStart: The hour at which the daytime rate starts (e.g., “06:00”).
 * nightStart: The hour at which the nighttime rate starts (e.g., “22:00”).
 * Houses can reference a tariff. The system uses day/night boundaries to compute cost in UsageRecord.
 */
@Entity
@Getter
@Setter
public class Tariff extends BaseAuditingEntity {
    String name;
    float dayRate_kWh;   // PLN/kWh
    float nightRate_kWh; // PLN/kWh
    LocalDateTime dayStart;     // e.g. "06:00"
    LocalDateTime nightStart;    // e.g. "22:00"
    // You could also store weekend rates if needed
}
