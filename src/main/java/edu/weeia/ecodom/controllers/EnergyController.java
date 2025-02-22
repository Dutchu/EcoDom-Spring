package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.api.v1.model.DailyDeviceEnergyDto;
import edu.weeia.ecodom.services.DeviceEnergyService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/houses/{houseId}/energy")
public class EnergyController {
    private final DeviceEnergyService energyService;

    public EnergyController(DeviceEnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping
    public List<DailyDeviceEnergyDto> getEnergyData(
            @PathVariable Long houseId,
            @RequestParam(defaultValue = "7") int days
    ) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        return startDate.datesUntil(endDate.plusDays(1))
                .map(date -> energyService.getDailyDeviceEnergy(houseId, date))
                .collect(Collectors.toList());
    }
}

