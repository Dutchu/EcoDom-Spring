package edu.weeia.ecodom.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;


@Configuration
@ConfigurationProperties(prefix = "breeds")
public class BreedsConfig {
    private Set<String> breeds;

    public Set<String> getBreeds() {
        return breeds;
    }

    public void setBreeds(Set<String> breeds) {
        this.breeds = breeds;
    }
}
