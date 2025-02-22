package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/***
 * deviceId: References the device.
 * dayOfWeek: Day for which the schedule is valid (e.g. “Monday”).
 * startTime / endTime: Hours of operation for the device on that day (e.g. 08:00 to 10:30).
 * The schedule helps your simulator or scheduler figure out how long a device runs on each day and automatically calculate consumption.
 */
@Entity
@Getter
@Setter
public class UsageHistory extends BaseAuditingEntity {
    @ManyToOne
    Device device;
    @OneToMany(fetch = FetchType.LAZY)
    Set<UsageRecord> usageRecords;
    LocalDateTime startTime; // e.g. "isActive on Device from false to true"
    LocalDateTime endTime;   // e.g. "isActive on Device from true to false"
    Float energyUsed_Wh; // Aggregate data from
}
