package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Device;
import edu.weeia.ecodom.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {


//    List<Device> findAllDevicesByAppUserUsername(String username);

    @Query("SELECT d FROM House h JOIN h.devices d WHERE h.appUser.username = :username")
    List<Device> findAllDevicesByUsername(@Param("username") String username);

    Optional<House> findFirstByAppUserUsername(String username);
}

