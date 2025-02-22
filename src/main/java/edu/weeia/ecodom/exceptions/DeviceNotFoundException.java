package edu.weeia.ecodom.exceptions;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long deviceId) {
        super("Device with id: " + deviceId + " was not found.");
    }
}
