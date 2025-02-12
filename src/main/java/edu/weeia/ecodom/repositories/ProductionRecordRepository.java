package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.ProductionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRecordRepository extends JpaRepository<ProductionRecord, Long> {
}