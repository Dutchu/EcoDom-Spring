package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.api.v1.mapper.HouseMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import edu.weeia.ecodom.repositories.HouseRepository;
import edu.weeia.ecodom.api.v1.model.HouseDto;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseRepository houseRepository;

    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @GetMapping
    public List<HouseDto> getAllHouses() {
        return houseRepository.findAll()
                .stream()
                .map(HouseMapper.INSTANCE::toGetUserHousesDto)
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/create")
    public String createHouse() {
        return "House Created";
    }

//    @PostMapping
//    public ResponseEntity<HouseDTO> createHouse(@Valid @RequestBody HouseDTO houseDTO) {
//        House house = new House();
//        // Map fields from DTO to entity.
//        house.setAddress(houseDTO.address());
//        house.setName(houseDTO.name());
//        // For relationships like owner and tariff, load the entities by ID, if needed.
//        House savedHouse = houseRepository.save(house);
//        return ResponseEntity.ok(convertToDto(savedHouse));
//    }
//
//    private HouseDTO convertToDto(House house) {
//        return new HouseDTO(
//                house.getId(),
//                house.getAddress(),
//                house.getName(),
//                house.getOwner() != null ? house.getOwner().getId() : null,
//                house.getDevices().stream().map(device -> device.getId()).collect(Collectors.toSet()),
//                house.getTariff() != null ? house.getTariff().getId() : null,
//                house.getCreatedAt(),
//                house.getUpdatedAt()
//        );
//    }
}
