package edu.weeia.ecodom.domain;

import org.springframework.context.ApplicationEvent;

public class DeviceStateChangeEvent extends ApplicationEvent {
    private final Device device;
    private final Boolean oldState;
    private final Boolean newState;

    public DeviceStateChangeEvent(Object source, Device device, Boolean oldState, Boolean newState) {
        super(source);
        this.device = device;
        this.oldState = oldState;
        this.newState = newState;
    }

    public Device getDevice() {
        return device;
    }

    public Boolean getOldState() {
        return oldState;
    }

    public Boolean getNewState() {
        return newState;
    }
}
