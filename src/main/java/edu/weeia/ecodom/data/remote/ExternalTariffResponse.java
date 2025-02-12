package edu.weeia.ecodom.data.remote;

public record ExternalTariffResponse(
        float dayRateKWh,
        float nightRateKWh,
        String dayStart,
        String nightStart
) {}
