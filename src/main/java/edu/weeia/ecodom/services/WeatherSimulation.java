package edu.weeia.ecodom.services;

import edu.weeia.ecodom.domain.WeatherCondition;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
public class WeatherSimulation {
    // A mapping from each weather condition to its baseline sun output factor.
    private final Map<WeatherCondition, Double> baselineFactors;
    @Setter
    private WeatherCondition currentCondition;

    public WeatherSimulation() {
        baselineFactors = new EnumMap<>(WeatherCondition.class);
        // Set default baseline factors for each weather condition.
        // These values can be adjusted via the controller as needed.
        baselineFactors.put(WeatherCondition.RAINY, 0.4);
        baselineFactors.put(WeatherCondition.CLOUDY, 0.7);
        baselineFactors.put(WeatherCondition.SUNNY, 1.0);
    }

    /**
     * Returns the baseline factor for the given condition.
     */
    public double getBaselineFactor(WeatherCondition condition) {
        return baselineFactors.getOrDefault(condition, 1.0);
    }

    /**
     * Returns a sun output factor for the specified weather condition.
     * The returned factor is calculated by applying a random variation of ±33% to the baseline.
     *
     * @return The sun output factor.
     */
    public double getSunOutputFactor() {
        double baseline = getBaselineFactor(this.currentCondition);
        // Generate a random variation between -variance and +variance.
        // Variance factor: 33% (i.e. the output will vary ±33% around the baseline)
        double variance = 0.33;
        double randomVariation = (Math.random() * (2 * variance)) - variance;
        return baseline * (1 + randomVariation);
    }
}
