package edu.weeia.ecodom.api.v1.mapper;

import edu.weeia.ecodom.api.v1.model.HouseDto;
import edu.weeia.ecodom.api.v1.model.UsageHistoryDto;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.domain.UsageHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UsageHistoryMapper {
    UsageHistoryMapper INSTANCE = Mappers.getMapper(UsageHistoryMapper.class);

    @Mapping(target = "startTime", source = "startTime")
    @Mapping(target = "endTime", source = "endTime")
    @Mapping(target = "energyUsed_Wh", source = "energyUsed_Wh")
    UsageHistoryDto mapToDto(UsageHistory history);
}
