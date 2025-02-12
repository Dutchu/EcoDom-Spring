package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {
}