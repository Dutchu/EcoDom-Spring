package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.Devices;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.domain.UsageHistory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.chrono.ChronoLocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BootstrapAdminService {
    BootstrapAdminService() {

    }

    @Bean
    @Order(2)
    public CommandLineRunner initAdminData(UserService userService,
                                           HouseService houseService,
                                           DeviceService deviceService,
                                           SimulationService simulationService, UsageHistoryService usageHistoryService) {
        return args -> {
            var foundAdmin = userService.findWithAuthoritiesByUsername("admin");

            var house = new House();
            house.setAppUser(foundAdmin);
            house.setName("Example House");
            house.setAddress("Example Street");
            var savedHouse = houseService.save(house);

            var deviceType = deviceService.findDeviceType(Devices.PC);
            var device = new Device();
            device.setIsActive(true);
            device.setNominalPower_W(Devices.PC.getWatts().floatValue());
            device.setType(deviceType);
            device.setHouse(savedHouse);
            device.setIsActive(false);
            var savedDevice = deviceService.save(device);

            var startDate = LocalDateTime.now().minusDays(30L);
            var endDate = LocalDateTime.now();
            List<UsageHistory> historyList = Stream.iterate(startDate, current -> current.isBefore(endDate), current -> current.plusDays(1))
                .map(currentStart -> {
                    // Each period lasts 23:59 from the current start time.
                    LocalDateTime currentEnd = currentStart.plusDays(23).plusMinutes(59);

                    // Ensure the period end does not exceed the overall endDate.
                    if (currentEnd.isAfter(ChronoLocalDateTime.from(endDate))) {
                        currentEnd = endDate;
                    }

                    // Calculate consumption for the current period.
                    double consumption = simulationService.calculateConsumption(savedDevice, currentStart, currentEnd);

                    // Create and return a new UsageHistory.
                    UsageHistory history = new UsageHistory();
                    history.setDevice(savedDevice);
                    history.setStartTime(currentStart);
                    history.setEndTime(currentEnd);
                    history.setEnergyUsed_Wh((float) consumption);
                    return history;
                })
                .toList();
            historyList.forEach(usageHistoryService::save);
            savedDevice.setUsageHistory(historyList);
            deviceService.save(savedDevice);
        };
    }
}
