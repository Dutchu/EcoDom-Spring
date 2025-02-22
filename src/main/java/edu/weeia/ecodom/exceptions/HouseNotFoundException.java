package edu.weeia.ecodom.exceptions;

public class HouseNotFoundException extends RuntimeException {
    public HouseNotFoundException(Long houseId) {
        super("Couldn't find House with ID: " + houseId);
    }
}
