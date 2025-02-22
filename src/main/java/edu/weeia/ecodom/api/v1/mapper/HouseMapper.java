package edu.weeia.ecodom.api.v1.mapper;

import edu.weeia.ecodom.api.v1.model.HouseDto;
import edu.weeia.ecodom.domain.House;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HouseMapper {
    HouseMapper INSTANCE = Mappers.getMapper(HouseMapper.class);

//    CreateHouseDto toGetUserHousesDto(House house);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "address", target = "address")
    HouseDto mapToDto(House house);
}
