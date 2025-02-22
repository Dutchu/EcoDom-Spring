package edu.weeia.ecodom.repositories;

import edu.weeia.ecodom.domain.Device;

public class UsageHistoryNotFoundException extends RuntimeException {
    public UsageHistoryNotFoundException(Device device) {
        super("Couldn't find usage history for device: " + device.getId() + ", Type Name: " + device.getType().getName());
    }
}
