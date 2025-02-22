package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.repositories.projections.SumEnergyByDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsageHistoryRepository extends JpaRepository<UsageHistory, Long> {

    @Query("SELECT uh FROM UsageHistory uh WHERE uh.device = :device AND uh.endTime IS NULL")
    Optional<UsageHistory> findActiveHistoryByDevice(@Param("device") Device device);
    Optional<UsageHistory> findByDeviceAndEndTimeIsNull(Device device);

    @Query("SELECT new edu.weeia.ecodom.repositories.projections.SumEnergyByDevice(uh.device, SUM(uh.energyUsed_Wh)) " +
            "FROM UsageHistory uh " +
            "WHERE uh.device = :device AND uh.energyUsed_Wh IS NOT NULL " +
            "GROUP BY uh.device")
    Optional<SumEnergyByDevice> sumEnergyUsedByDevice(@Param("device") Device device);
    List<UsageHistory> findAllByDevice(Device device);

//    List<UsageHistory> findByHouseAndDate(House house, LocalDate date);
    List<UsageHistory> findByDeviceHouseAndStartTimeBetween(House house, LocalDateTime start, LocalDateTime end);

//    List<UsageHistory> findByHouseAndDate(House house, LocalDate date);
}
