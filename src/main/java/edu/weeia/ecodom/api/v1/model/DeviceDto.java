package edu.weeia.ecodom.api.v1.model;

import java.time.LocalDateTime;

public record DeviceDto(
        long id,
        String deviceType,
        Float nominalPower_W
// From different controller. Needs to be as live data
//        Float consumption_W
//        boolean isActive
//        LocalDateTime updateTimestamp
) {
}
