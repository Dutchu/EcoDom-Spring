package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.UsageRecord;
import edu.weeia.ecodom.repositories.UsageRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UsageRecordService {
    private final UsageRecordRepository usageRecordRepository;

    public List<UsageRecord> findByDeviceAndTimestampBetween(Device device, LocalDateTime startTime, LocalDateTime endTime) {
        return usageRecordRepository.findByDeviceAndTimestampBetween(device, startTime, endTime);
    }

    public void save(UsageRecord record) {
        usageRecordRepository.save(record);
    }
}
