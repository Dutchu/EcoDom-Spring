package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.DeviceType;
import edu.weeia.ecodom.domain.Devices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {
    Optional<DeviceType> findFirstByName(Devices name);
}
