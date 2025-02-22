package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.api.v1.model.DeviceCreateDto;
import edu.weeia.ecodom.api.v1.model.DeviceDetailsDto;
import edu.weeia.ecodom.api.v1.model.DeviceDto;
import edu.weeia.ecodom.api.v1.model.DeviceStatusDto;
import edu.weeia.ecodom.security.services.UserDetailsImpl;
import edu.weeia.ecodom.services.DeviceService;
import edu.weeia.ecodom.services.websocket.DeviceStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceStateManager deviceStateManager;

    @GetMapping("/devices/ecodom")
    List<String> systemDeviceTypes() {
        return deviceService.findSystems();
    }

    @GetMapping("/houses/{houseId}/devices")
    List<DeviceDto> findHouseDevices(@PathVariable Long houseId,
                                     @AuthenticationPrincipal UserDetailsImpl principal) {
        return deviceService.findHouseDevicesByUser(houseId, principal.getUsername());
    }

    @GetMapping("/houses/{houseId}/devices/{deviceId}")
    DeviceDetailsDto findHouseDevices(@PathVariable Long houseId,
                                           @PathVariable Long deviceId,
                                           @AuthenticationPrincipal UserDetailsImpl principal) {
        return deviceService.findDeviceDetails(deviceId, houseId, principal.getUsername());
    }

    @PostMapping("/houses/{houseId}/devices")
    DeviceDto createDevice(@PathVariable Long houseId,
                           @RequestBody DeviceCreateDto dto,
                           @AuthenticationPrincipal UserDetailsImpl principal) {
        return deviceService.createDeviceForHouse(houseId, principal.getUsername(), dto);
    }


    @GetMapping("/houses/{houseId}/devices/{deviceId}/live")
    public DeviceStatusDto getDeviceState(@PathVariable Long deviceId,
                                                          @PathVariable Long houseId) {
        return deviceService.getInitStatus(deviceId);
    }
}
