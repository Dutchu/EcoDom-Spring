package edu.weeia.ecodom.repositories.projections;

import edu.weeia.ecodom.domain.Device;

public record SumEnergyByDevice(Device device,
                                Double totalEnergyUsed) {
}
