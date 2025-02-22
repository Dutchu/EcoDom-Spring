package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.api.v1.model.HouseCreateDto;
import edu.weeia.ecodom.api.v1.model.HouseDto;
import edu.weeia.ecodom.security.model.response.MessageResponse;
import edu.weeia.ecodom.services.HouseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping
    public List<HouseDto> getAllHouses() {
        List<HouseDto> all = houseService.findAll();
        return all;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<?> createHouse(@AuthenticationPrincipal UserDetails principal,
                                         @Valid @RequestBody HouseCreateDto houseDto) {
        var createdHouse = houseService.create(houseDto, principal.getUsername());
        return ResponseEntity.ok().body(createdHouse);
    }

    @PostMapping(path = "/bootstrap")
    public ResponseEntity<?> bootstrap() {
        var result = ResponseEntity
                .status(500)
                .body(new MessageResponse("Something went wrong!"));
        try {
            houseService.bootstrap();
            result = ResponseEntity
                    .status(201)
                    .body(new MessageResponse("Git!"));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return result;
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
