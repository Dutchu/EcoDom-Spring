package edu.weeia.ecodom.services;

import edu.weeia.ecodom.api.v1.mapper.HouseMapper;
import edu.weeia.ecodom.api.v1.model.HouseCreateDto;
import edu.weeia.ecodom.api.v1.model.HouseDto;
import edu.weeia.ecodom.domain.AppUser;
import edu.weeia.ecodom.domain.House;
import edu.weeia.ecodom.exceptions.HouseNotFoundException;
import edu.weeia.ecodom.repositories.HouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HouseService {

    private final HouseRepository houseRepository;
    private final UserService userService;

    public List<HouseDto> findAll() {
        return houseRepository.findAll().stream()
                .map(HouseMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    public HouseDto create(HouseCreateDto houseDto, String username) {
        var user = userService.findWithAuthoritiesByUsername(username);

        House toSave = new House();
        toSave.setName(houseDto.name());
        toSave.setAddress(houseDto.address());
        toSave.setAppUser(user);

        return HouseMapper.INSTANCE.mapToDto(houseRepository.save(toSave));
    }


    public boolean bootstrap() {
        boolean result = false;
        String exampleUserName = "user1";
        AppUser foundUser = userService.findWithAuthoritiesByUsername(exampleUserName);

        House toSave = new House();
        toSave.setName("Example House");
        toSave.setAddress("Example Street");
        toSave.setAppUser(foundUser);
        houseRepository.save(toSave);

        return result;
    }

    public House findUserHouse(Long houseId, String username) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));
    }

    House save(House house) {
        return houseRepository.save(house);
    }

    public List<House> findAllHouses() {
        return houseRepository.findAll();
    }

    public House findById(Long houseId) {
        return houseRepository.findById(houseId)
                .orElseThrow(() -> new HouseNotFoundException(houseId));
    }
}
