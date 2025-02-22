package edu.weeia.ecodom.api.v1.model;

public record DeviceTypeDto(
        Long id,
        String name,
        Integer wattage
) {
}
