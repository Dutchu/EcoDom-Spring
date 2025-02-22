package edu.weeia.ecodom.services.websocket;

import edu.weeia.ecodom.api.v1.mapper.UsageHistoryMapper;
import edu.weeia.ecodom.api.v1.model.HouseEnergySummaryDto;
import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.domain.UsageRecord;
import edu.weeia.ecodom.services.DeviceService;
import edu.weeia.ecodom.services.SimulationService;
import edu.weeia.ecodom.services.UsageHistoryService;
import edu.weeia.ecodom.services.UsageRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class DeviceStateManager {
    private final DeviceService deviceService;
    private final UsageRecordService usageRecordService;
    private final UsageHistoryService usageHistoryService;
    private final SimulationService simulationService;
    private final DeviceStatusPublisher statusPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void updateDeviceState(Long deviceId, boolean newState) {
        Device device = deviceService.findById(deviceId);
        boolean oldState = device.getIsActive();

        if (oldState == newState) {
            return;
        }

        device.setIsActive(newState);
        deviceService.save(device);

        if (newState) {
            handleActivation(device);
        } else {
            handleDeactivation(device);
        }

        // Publish updated status
        statusPublisher.publishDeviceStatus(device);
    }

    private void handleActivation(Device device) {
        // Create new usage history record
        UsageHistory history = new UsageHistory();
        history.setDevice(device);
        history.setStartTime(LocalDateTime.now());
        var savedHistory = usageHistoryService.save(history);

        // Start simulation
        simulationService.startSimulationForDevice(device, savedHistory);
    }

    private void handleDeactivation(Device device) {
        // Find and complete the active history

        var history = usageHistoryService.findActiveHistoryByDevice(device);
        if (history != null) {
            LocalDateTime endTime = LocalDateTime.now();
            history.setEndTime(endTime);

            // Get all usage records for this period
            List<UsageRecord> records = usageRecordService.findByDeviceAndTimestampBetween(
                    device,
                    history.getStartTime(),
                    endTime
            );

            float energyUsedWh = 0f;
            if (!records.isEmpty()) {
                // Calculate total duration in hours
                long durationMillis = Duration.between(history.getStartTime(), endTime).toMillis();
                float durationHours = durationMillis / (1000f * 3600f);

                // Calculate the average power (in watts) during this period
                float sumPower = records.stream()
                        .map(UsageRecord::getConsumption_W)
                        .reduce(0f, Float::sum);
                float averagePower = sumPower / records.size();

                // Energy (in Wh) = Average Power (W) * Duration (h)
                energyUsedWh = averagePower * durationHours / 1000;
            }

            history.setEnergyUsed_Wh(energyUsedWh);

            history.setUsageRecords(new HashSet<>(records)); // Add reference to records
            usageHistoryService.save(history);

            // Stop simulation
            simulationService.stopSimulationForDevice(device);

            // Publish history update
            messagingTemplate.convertAndSend(
                    "/topic/house/" + device.getHouse().getId() + "/device/" + device.getId() + "/history",
                    UsageHistoryMapper.INSTANCE.mapToDto(history)
            );
        }
    }

    public HouseEnergySummaryDto getHouseEnergySummary(Long houseId, LocalDate date) {
        List<UsageHistory> histories = usageHistoryService.findByHouseAndDate(houseId, date);

        Map<Long, Double> deviceEnergy = new HashMap<>();
        double total = 0.0;

        for (UsageHistory history : histories) {
            double kWh = history.getEnergyUsed_Wh() / 1000.0;
            deviceEnergy.merge(history.getDevice().getId(), kWh, Double::sum);
            total += kWh;
        }

        return new HouseEnergySummaryDto(date, total, deviceEnergy);
    }
}
