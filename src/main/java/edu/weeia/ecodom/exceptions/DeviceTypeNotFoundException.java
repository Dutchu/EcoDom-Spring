package edu.weeia.ecodom.exceptions;

import edu.weeia.ecodom.domain.Devices;

public class DeviceTypeNotFoundException extends RuntimeException {
    public DeviceTypeNotFoundException(Devices name) {
        super("Couldn't find device with name: " + name);
    }
}
