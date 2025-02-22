package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.mapper.UsageHistoryMapper;
import edu.weeia.ecodom.api.v1.model.UsageHistoryDto;
import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.repositories.UsageHistoryNotFoundException;
import edu.weeia.ecodom.repositories.UsageHistoryRepository;
import edu.weeia.ecodom.repositories.projections.SumEnergyByDevice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsageHistoryService {
    private final UsageHistoryRepository usageHistoryRepository;
    private final HouseService houseService;

    public UsageHistoryService(UsageHistoryRepository usageHistoryRepository, HouseService houseService) {
        this.usageHistoryRepository = usageHistoryRepository;
        this.houseService = houseService;
    }

    public UsageHistory findActiveHistoryByDevice(Device device) {
        return usageHistoryRepository.findActiveHistoryByDevice(device)
                .orElseThrow(() -> new UsageHistoryNotFoundException(device));
    }

    public SumEnergyByDevice sumEnergyUsedByDevice(Device device) {
        return usageHistoryRepository.sumEnergyUsedByDevice(device)
                .orElseThrow(() -> new UsageHistoryNotFoundException(device));
    }

    public UsageHistory save(UsageHistory history) {
        return usageHistoryRepository.save(history);
    }

    public List<UsageHistory> findAllByDevice(Device device) {
        return usageHistoryRepository.findAllByDevice(device);
    }

    public List<UsageHistory> findByHouseAndDate(Long houseId, LocalDate date) {
        House foundHouse = houseService.findById(houseId);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        return usageHistoryRepository.findByDeviceHouseAndStartTimeBetween(foundHouse, startOfDay, endOfDay);
    }
}

