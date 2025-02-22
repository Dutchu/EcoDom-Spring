package edu.weeia.ecodom.api.v1.model;

import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.domain.UsageRecord;

import java.util.List;

public record DeviceDetailsDto(
        Long id,
        String type,
        Float nominalPower_W,
        Float powerUsed_Wh,
        List<UsageHistoryDto> usageHistory
) {
}
