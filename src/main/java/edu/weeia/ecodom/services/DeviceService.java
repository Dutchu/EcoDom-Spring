package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.mapper.DeviceMapper;
import edu.weeia.ecodom.api.v1.mapper.UsageHistoryMapper;
import edu.weeia.ecodom.api.v1.model.DeviceCreateDto;
import edu.weeia.ecodom.api.v1.model.DeviceDetailsDto;
import edu.weeia.ecodom.api.v1.model.DeviceDto;
import edu.weeia.ecodom.api.v1.model.DeviceStatusDto;
import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.DeviceType;
import edu.weeia.ecodom.domain.Devices;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.exceptions.DeviceNotFoundException;
import edu.weeia.ecodom.exceptions.DeviceTypeNotFoundException;
import edu.weeia.ecodom.repositories.DeviceRepository;
import edu.weeia.ecodom.repositories.DeviceTypeRepository;
import edu.weeia.ecodom.repositories.HouseRepository;
import edu.weeia.ecodom.repositories.UsageHistoryNotFoundException;
import edu.weeia.ecodom.repositories.projections.SumEnergyByDevice;
import edu.weeia.ecodom.security.jwt.AuthEntryPointJwt;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final HouseRepository houseRepository;
    private final HouseService houseService;
    private final DeviceTypeRepository deviceTypeRepository;
    private final AuthorityService authorityService;
    private final UsageHistoryService usageHistoryService;
    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    float getConsumption(DeviceType deviceType) {
        return 0.0f;
    }

    public List<String> findSystems() {
        return Arrays.stream(Devices.values()).map(Devices::name).collect(Collectors.toList());
    }



    public List<DeviceDto> findHouseDevicesByUser(Long houseId, String username) {

        var jpql = houseRepository.findAllDevicesByUsername(username);
//        var keywords = houseRepository.findAllDevicesByAppUserUsername(username);
        var deviceR = deviceRepository.findAllByHouseId(houseId);

        System.out.println("jpql: " + jpql + '\n' +
//                "keywords: " + keywords + '\n' +
                "deviceR: " + deviceR + '\n');

        return deviceR.stream()
                .map(DeviceMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    public DeviceDto createDeviceForHouse(Long houseId, String username, DeviceCreateDto dto) {
        var foundHouse = houseService.findUserHouse(houseId, username);
        var name = Devices.valueOf(dto.deviceType());
        var foundType = deviceTypeRepository.findFirstByName(name)
                .orElseThrow(() -> new DeviceTypeNotFoundException(name));

        var deviceToSave = new Device();
        deviceToSave.setHouse(foundHouse);
        deviceToSave.setType(foundType);
        deviceToSave.setNominalPower_W(dto.nominalPower_W());
        deviceToSave.setIsActive(false);
        return DeviceMapper.INSTANCE.mapToDto(deviceRepository.save(deviceToSave));
    }

    @PostConstruct
    public void initData() {
        Arrays.stream(Devices.values())
                .forEach(e -> {
                    var d = new DeviceType();
                    d.setName(e);
                    d.setWattage(e.getWatts());
                    deviceTypeRepository.save(d);
                });

    }

    public DeviceDetailsDto findDeviceDetails(Long deviceId, Long houseId, String username) {
        var foundHouse = houseService.findUserHouse(houseId, username);
        var foundDevice = deviceRepository.findFirstById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        //TODO: Finish DeviceDetailsDto construction
        SumEnergyByDevice energyUsed;
         try {
             energyUsed = usageHistoryService.sumEnergyUsedByDevice(foundDevice);

         } catch (UsageHistoryNotFoundException e) {
             logger.error(e.getMessage());
             energyUsed = new SumEnergyByDevice(
                     foundDevice,
                     0.0d);
         }
        var history = usageHistoryService.findAllByDevice(foundDevice).stream()
                .map(UsageHistoryMapper.INSTANCE::mapToDto)
                .toList();

        return new DeviceDetailsDto(
                deviceId,
                foundDevice.getType().getName().name(),
                foundDevice.getNominalPower_W(),
                energyUsed.totalEnergyUsed().floatValue(),
                history
        );
    }

    public DeviceType findDeviceType(Devices type) {
        return deviceTypeRepository.findFirstByName(type)
                .orElseThrow(() -> new DeviceTypeNotFoundException(type));
    }

    public Device findDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
    }

    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    public Device findById(Long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    public List<Device> findAllByHouse(House house) {
        return deviceRepository.findAllByHouse(house);
    }

    public DeviceStatusDto getInitStatus(Long deviceId) {
        var foundDevice = this.findById(deviceId);
        var isActive = foundDevice.getIsActive();

        return new DeviceStatusDto(
                foundDevice.getId(),
                foundDevice.getType().getName().name(),
                foundDevice.getIsActive(),
                0f,
                LocalDateTime.now()
        );
    }

}
