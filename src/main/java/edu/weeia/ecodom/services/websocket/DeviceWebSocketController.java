package edu.weeia.ecodom.services.websocket;

import edu.weeia.ecodom.api.v1.model.DeviceStatusDto;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@AllArgsConstructor
@Controller
public class DeviceWebSocketController {
    private final DeviceStateManager deviceStateManager;

    // DeviceWebSocketController.java
    @MessageMapping("/house/{houseId}/device/{deviceId}/toggle")
    public void toggleDevice(
            @DestinationVariable Long houseId,
            @DestinationVariable Long deviceId,
            DeviceStatusDto request
    ) {
        // Remove @SendTo annotation
        deviceStateManager.updateDeviceState(deviceId, request.isActive());
    }
}