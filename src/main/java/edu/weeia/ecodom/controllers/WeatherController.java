package edu.weeia.ecodom.controllers;

import edu.weeia.ecodom.domain.WeatherCondition;
import edu.weeia.ecodom.services.WeatherSimulation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/weather")
public class WeatherController {


    private final WeatherSimulation weatherSimulation;
    /**
     * Updates the current weather condition.
     * You can call this endpoint with a PATCH request, for example:
     * PATCH /api/weather/currentCondition?condition=SUNNY
     *
     * @param condition the new weather condition (RAINY, CLOUDY, or SUNNY)
     * @return a ResponseEntity confirming the update
     */
    @PatchMapping("/currentCondition")
    public ResponseEntity<String> updateCurrentCondition(@RequestParam("condition") WeatherCondition condition) {
        weatherSimulation.setCurrentCondition(condition);
        return ResponseEntity.ok("Current weather condition updated to: " + condition);
    }
}