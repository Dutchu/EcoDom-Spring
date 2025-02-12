package edu.weeia.ecodom.security.model.request;

public record LoginRequest(
        String username,
        String password
) {
}
