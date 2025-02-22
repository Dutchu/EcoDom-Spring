package edu.weeia.ecodom.api.v1.model;

public record DeviceCreateDto(
        String deviceType,
        Float nominalPower_W
) {
}
