package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.PhotovoltaicSystem;
import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.domain.UsageRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class SimulationService {
    //    private final DeviceService deviceService;
    private static final Random rand = new Random();
    private final TaskScheduler taskScheduler;
    // Map to track simulation tasks per device
    private final Map<Long, ScheduledFuture<?>> simulationTasks = new ConcurrentHashMap<>();
    private final DeviceService deviceService;
    private final UsageRecordService usageRecordService;

    public SimulationService(@Qualifier("threadPoolTaskScheduler") TaskScheduler taskScheduler,
                             DeviceService deviceService,
                             UsageRecordService usageRecordService) {
        this.taskScheduler = taskScheduler;
        this.deviceService = deviceService;
        this.usageRecordService = usageRecordService;
    }


    public static double getConsumption(Device device) {
        double randomPercentage = (Math.random() * 0.2) - 0.1;
        return (device.getNominalPower_W() * randomPercentage) + device.getNominalPower_W();
    }

    static double getProduction(PhotovoltaicSystem photovoltaicSystem, WeatherSimulation weatherSimulation) {
        return (photovoltaicSystem.getMaxPower_kW() * weatherSimulation.getSunOutputFactor()) + photovoltaicSystem.getMaxPower_kW();
    }

    /**
     * Starts simulation for the given device if not already running.
     */
    public void startSimulationForDevice(Device device, UsageHistory usageHistory) {
        if (simulationTasks.containsKey(device.getId())) {
            return; // Simulation is already running for this device.
        }
        ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(() -> {
            Device refreshedDevice = deviceService.findDeviceById(device.getId());
//            if (refreshedDevice == null || !Boolean.TRUE.equals(refreshedDevice.getIsActive())) {
//                break;
//            }
            double consumption = getConsumption(refreshedDevice);

            // Create and save a new UsageRecord.
            UsageRecord record = new UsageRecord();
            record.setDevice(refreshedDevice);
            record.setUsageHistory(usageHistory);
            record.setTimestamp(LocalDateTime.now());
            record.setConsumption_W((float) consumption);
            usageRecordService.save(record);
        }, Duration.ofSeconds(5));
        simulationTasks.put(device.getId(), future);
    }

    /**
     * Stops the simulation task for the given device.
     */
    public void stopSimulationForDevice(Device device) {
        ScheduledFuture<?> future = simulationTasks.remove(device.getId());
        if (future != null) {
            future.cancel(true);
        }
    }

    public double calculateConsumption(Device device, LocalDateTime currentStart, LocalDateTime currentEnd) {
        // Calculate the duration of the period in minutes, then convert to hours
        long minutes = Duration.between(currentStart, currentEnd).toMinutes();
        float hours = minutes / 60.0f;
        // Retrieve the device's power consumption in Watts
        var watts = getConsumption(device);
        // Calculate consumption in kWh:
        // (Watts * hours) / 1000 = kWh
        return (watts * hours) / 1000d;
    }
}
