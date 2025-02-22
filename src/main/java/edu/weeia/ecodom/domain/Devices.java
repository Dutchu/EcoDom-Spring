package edu.weeia.ecodom.domain;

public enum Devices {
    FRIDGE(800),
    OVEN(4000),
    WASHING_MACHINE(1400),
    TV(80),
    LIGHTS(100),
    PC(400);

    private final Integer watts;

    Devices(Integer watts) {
        this.watts = watts;
    }

    public Integer getWatts() {
        return this.watts;
    }
}
