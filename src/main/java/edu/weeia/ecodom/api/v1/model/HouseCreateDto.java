package edu.weeia.ecodom.api.v1.model;

import jakarta.validation.constraints.NotNull;

public record HouseCreateDto(
        @NotNull
        String name,
        @NotNull
        String address
) {}
