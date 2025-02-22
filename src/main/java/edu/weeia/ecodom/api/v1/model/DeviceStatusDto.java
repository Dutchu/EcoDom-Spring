package edu.weeia.ecodom.api.v1.model;

import java.time.LocalDateTime;

public record DeviceStatusDto(
        Long deviceId,
        String deviceName, // Added for frontend labeling
        Boolean isActive,
        Float consumption_W,
//        Float energyUsed_kWh,
        LocalDateTime updateTimestamp
) {
}
