package edu.weeia.ecodom.api.v1.model;

import java.time.LocalDateTime;

public record UsageHistoryDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        Float energyUsed_Wh
) {
}
