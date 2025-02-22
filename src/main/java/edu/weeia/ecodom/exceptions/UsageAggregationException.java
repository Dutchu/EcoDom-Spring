package edu.weeia.ecodom.exceptions;

import edu.weeia.ecodom.domain.Device;

import java.time.LocalDateTime;

public class UsageAggregationException extends RuntimeException {
    public UsageAggregationException(Device device, LocalDateTime startTime, LocalDateTime endTime) {
        super(String.format("""
                Couldn't aggregate data for Device {
                \tid: %s,
                \tname: %s }
                Period {
                \tfrom: %s 
                \tto: %s }
                """,
                device.getId(),
                device.getType().getName(),
                startTime,
                endTime
        ));
    }
}
