package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.DeviceType;
import edu.weeia.ecodom.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAllByHouseId(Long id);
    Optional<Device> findFirstById(Long Id);

    List<Device> findAllByHouse(House house);
}