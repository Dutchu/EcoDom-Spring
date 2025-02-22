package edu.weeia.ecodom.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/***
 * deviceId: References the device.
 * date: The specific date of usage.
 * startTime / endTime: Time window of actual usage.
 * totalConsumption_kWh: Calculated energy usage in kWh for that record.
 * cost: Total cost based on the relevant day/night tariff or any real-time calculation.
 * Depending on your design, these UsageRecords might be automatically generated from Schedule + nominalPower_kW + the day/night tariffs.
 */
@Entity
@Getter
@Setter
public class UsageRecord extends BaseAuditingEntity {
//
    @ManyToOne
    Device device;
    @ManyToOne
    UsageHistory usageHistory;
    LocalDateTime timestamp;
    Float consumption_W;
}
