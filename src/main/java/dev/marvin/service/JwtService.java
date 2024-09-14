package dev.marvin.service;

import org.springframework.security.core.Authentication;

import java.util.Map;

public interface JwtService {
    String generateToken(Authentication authentication);
    Boolean validateToken(String token);
    Map<String, Object> extractClaimsFromToken(String token);
}
