package edu.weeia.ecodom.api.v1.mapper;

import edu.weeia.ecodom.api.v1.model.HouseDto;
import edu.weeia.ecodom.domain.House;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HouseMapper {
    HouseMapper INSTANCE = Mappers.getMapper(HouseMapper.class);

    HouseDto toGetUserHousesDto(House house);
}
