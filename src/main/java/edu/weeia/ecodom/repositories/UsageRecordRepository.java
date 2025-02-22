package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.UsageHistory;
import edu.weeia.ecodom.domain.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
    List<UsageRecord> findByDeviceAndTimestampBetween(Device device, LocalDateTime startTime, LocalDateTime endTime);
}