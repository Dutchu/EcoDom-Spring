package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
