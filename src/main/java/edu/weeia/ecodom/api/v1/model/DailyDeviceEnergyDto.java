package edu.weeia.ecodom.api.v1.model;

import java.time.LocalDate;
import java.util.Map;

public record DailyDeviceEnergyDto(
        LocalDate date,
        // Map<DeviceId, Energy in kWh>
        Map<Long, DeviceEnergy> devices
) {
}
