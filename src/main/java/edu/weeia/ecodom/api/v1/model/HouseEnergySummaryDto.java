package edu.weeia.ecodom.api.v1.model;

import java.time.LocalDate;
import java.util.Map;

public record HouseEnergySummaryDto(
        LocalDate date,
        Double totalEnergyKWh,
        Map<Long, Double> deviceEnergyKWh // Device-specific breakdown
) {
}