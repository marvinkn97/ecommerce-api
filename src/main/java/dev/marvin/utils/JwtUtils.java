package dev.marvin.utils;


import dev.marvin.domain.SecurityConstants;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.exception.RequestValidationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {
    public String generateToken(Authentication authentication) {
        log.info("Inside generateToken method of JwtUtils");

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String email = userPrincipal.getUsername();

        Date date = SecurityConstants.ISSUED_AT;
        log.info("iat: {}", date);

        Date expirationDate = SecurityConstants.EXPIRES_AT;
        log.info("exp: {}", expirationDate);

        return Jwts.builder()
                .setIssuedAt(date)
                .setSubject(email)
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String getTokenFromHeader(HttpServletRequest request) {
        log.info("Inside getJwtFromHeader method of JwtUtils");
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public Boolean validateToken(String token) {
        log.info("Inside validateToken method of JwtUtils");
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);

            if (!claimsJws.getBody().getIssuer().equals(SecurityConstants.TOKEN_ISSUER)) {
                log.error("Token issuer is invalid");
                return false;
            }
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token has expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Token is malformed: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            throw new RequestValidationException("Invalid token", e);
        }
    }

    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder().build().parseClaimsJws(token).getBody().getSubject();
    }

    public Map<String, Object> extractClaimsFromToken(String token) {
        log.info("Inside extractClaimsFromToken method of JwtUtils");
        try {
            Claims claims = Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
            return new HashMap<>(claims);
        } catch (JwtException e) {
            log.error("Error occurred in extractClaimsFromToken method of JwtUtils: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }

    }
}
