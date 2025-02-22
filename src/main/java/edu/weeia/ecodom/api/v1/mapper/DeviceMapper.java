package edu.weeia.ecodom.api.v1.mapper;

import edu.weeia.ecodom.api.v1.model.DeviceDto;
import edu.weeia.ecodom.api.v1.model.DeviceStatusDto;
import edu.weeia.ecodom.api.v1.model.DeviceTypeDto;
import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.DeviceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DeviceMapper {
    DeviceMapper INSTANCE = Mappers.getMapper(DeviceMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "wattage", source = "wattage")
    DeviceTypeDto mapToTypeDto(DeviceType deviceType);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "deviceType", source = "type", qualifiedByName = "stringFromDeviceType")
    @Mapping(target = "nominalPower_W", source = "nominalPower_W")
    DeviceDto mapToDto(Device device);

    @Named("stringFromDeviceType")
    static String stringFromDeviceType(DeviceType type) {
        return type.getName().name();
    }
}
