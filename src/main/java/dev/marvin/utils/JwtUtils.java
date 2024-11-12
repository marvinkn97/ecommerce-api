package dev.marvin.utils;


import dev.marvin.domain.UserPrincipal;
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

    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String ISSUER = "dev.marvin";

    public String generateToken(Authentication authentication) {
        log.info("Inside generateToken method of JwtUtils");

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String mobile = userPrincipal.getUsername();

        String role = userPrincipal.userEntity().getRoleEnum().name();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        Date date = new Date(System.currentTimeMillis());
        log.info("iat: {}", date);

        Date expirationDate = new Date(date.getTime() + (1000 * 60 * 60));
        log.info("exp: {}", expirationDate);

        return Jwts.builder()
                .setSubject(mobile)
                .setIssuedAt(date)
                .setIssuer(ISSUER)
                .setExpiration(expirationDate)
                .addClaims(claims)
                .signWith(SECRET_KEY)
                .compact();
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
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return claimsJws.getBody().getIssuer().equals(ISSUER);

        } catch (ExpiredJwtException e) {
            log.error("Token has expired: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("Token is malformed: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage(), e);
            return false;
        }
    }

    public Map<String, Object> extractClaimsFromToken(String token) {
        log.info("Inside extractClaimsFromToken method of JwtUtils");
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            return new HashMap<>(claims);
        } catch (JwtException e) {
            log.error("Error occurred in extractClaimsFromToken method of JwtUtils: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }

    }
}
