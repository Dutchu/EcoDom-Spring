package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TariffRepository extends JpaRepository<Tariff, Long> {
}