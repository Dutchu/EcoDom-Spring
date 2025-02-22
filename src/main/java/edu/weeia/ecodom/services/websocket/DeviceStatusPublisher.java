package edu.weeia.ecodom.services.websocket;

import edu.weeia.ecodom.api.v1.model.DeviceStatusDto;
import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.services.DeviceService;
import edu.weeia.ecodom.services.HouseService;
import edu.weeia.ecodom.services.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// DeviceStatusPublisher.java
@Service
@RequiredArgsConstructor
public class DeviceStatusPublisher {
    private final SimpMessagingTemplate messagingTemplate;
    private final DeviceService deviceService;
    private final SimulationService simulationService;
    private final HouseService houseService;

    @Operation(
            summary = "Subscribe to real-time device status updates",
            description = "Clients can subscribe to `/topic/house/{houseId}/devices` to receive updates on device statuses.",
            responses = @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Returns a list of devices with their statuses.",
                    content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = DeviceStatusDto.class))
                    )
            )
    )
    @MessageMapping("/house/{houseId}/devices") // Client sends message here
    @SendTo("/topic/house/{houseId}/devices") // Clients subscribe here
    @Scheduled(fixedRate = 5000) // 5-second heartbeat
    public void publishHouseStatuses() {
        // Get all houses (you might want to optimize this)
        List<House> houses = houseService.findAllHouses();

        for (House house : houses) {
            //HOUSE WIDE CONSUMPTION
            List<DeviceStatusDto> statuses = getDeviceStatuses(house);
            messagingTemplate.convertAndSend(
                    "/topic/house/" + house.getId() + "/devices",
                    statuses
            );
            //HOUSE WIDE ENERGY
        }
    }

    public void publishDeviceStatus(Device device) {
        DeviceStatusDto status = createDeviceStatus(device);
        messagingTemplate.convertAndSend(
                "/topic/house/" + device.getHouse().getId() + "/device/" + device.getId(),
                status
        );
    }

    private List<DeviceStatusDto> getDeviceStatuses(House house) {
        return deviceService.findAllByHouse(house)
                .stream()
                .map(this::createDeviceStatus)
                .collect(Collectors.toList());
    }

    private DeviceStatusDto createDeviceStatus(Device device) {
        float consumption = Boolean.TRUE.equals(device.getIsActive())
                ? (float) SimulationService.getConsumption(device)
                : 0f;

        return new DeviceStatusDto(
                device.getId(),
                device.getType().getName().name(),
                device.getIsActive(),
                consumption,
                LocalDateTime.now()
        );
    }
}