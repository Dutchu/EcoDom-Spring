package edu.weeia.ecodom.api.v1.model;

import edu.weeia.ecodom.domain.Authority;

import java.util.Set;

public record UserCreatedDto(Long Id, String username, String email, Set<Authority> role) {}
