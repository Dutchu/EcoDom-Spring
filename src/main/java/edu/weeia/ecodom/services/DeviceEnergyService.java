package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.model.DailyDeviceEnergyDto;
import edu.weeia.ecodom.api.v1.model.DeviceEnergy;
import edu.weeia.ecodom.domain.UsageHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeviceEnergyService {
    private final UsageHistoryService usageHistoryService;
    private final DeviceService deviceService;

    public DailyDeviceEnergyDto getDailyDeviceEnergy(Long houseId, LocalDate date) {
        List<UsageHistory> histories = usageHistoryService.findByHouseAndDate(houseId, date);

        Map<Long, DeviceEnergy> deviceEnergyMap = histories.stream()
                .collect(Collectors.groupingBy(
                        history -> history.getDevice().getId(),
                        Collectors.summingDouble(history -> history.getEnergyUsed_Wh() / 1000.0)
                ))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new DeviceEnergy(
                                deviceService.findById(entry.getKey()).getType().getName().name(),
                                entry.getValue()
                        )
                ));

        return new DailyDeviceEnergyDto(date, deviceEnergyMap);
    }
}
