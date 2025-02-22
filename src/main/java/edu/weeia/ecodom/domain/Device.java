package edu.weeia.ecodom.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    Boolean isActive = false;
    Float nominalPower_W;

    @ManyToOne
    private DeviceType type;

    @ManyToOne
    House house;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    List<UsageHistory> usageHistory = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    Set<UsageRecord> usageRecords = new HashSet<>();
}
