package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/***
 * houseId: References the house where the device is located.
 * name: Name of the device (e.g. “Pralka”, “Lodówka”, “Grzejnik elektryczny”).
 * nominalPower_kW: Nominal power usage in kW (e.g., 1.2 for a heater).
 * Each device can have one or more schedules (SCHEDULE) and multiple usage records (USAGE_RECORD) to track real or simulated usage.
 */
@Entity
@Getter
@Setter
public class Device extends BaseAuditingEntity {
    @ManyToOne
    House houseId;
    @ManyToOne
    Schedule scheduleId;
    @OneToMany(mappedBy = "deviceId")
    Set<UsageRecord> usageRecords = new HashSet<>();
    String name;
    Float nominalPower_kW;
}
