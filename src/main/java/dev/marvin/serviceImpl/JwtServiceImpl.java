package dev.marvin.serviceImpl;

import dev.marvin.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final Key key;
    public JwtServiceImpl() {
        Key key = Jwts.SIG.HS256.key().build();
        this.key = Keys.hmacShaKeyFor(key.getEncoded());
    }

    @Override
    public String generateToken(Authentication authentication) {
        log.info("Inside generateToken method of JwtServiceImpl");
        try {
            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }
            Date date = new Date();
            log.info("iat: {}", date);

            Date expirationDate = new Date(date.getTime() + 1000 * 60);
            log.info("exp: {}", expirationDate);

            Map<String, Object> claimsMap = new HashMap<>();

            return Jwts.builder()
                    .issuedAt(date)
                    .subject(authentication.getName())
                    .expiration(expirationDate)
                    .claims(claimsMap)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            log.error("Unexpected Error Occurred in generateToken method of JwtServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }

    }

    @Override
    public Boolean validateToken(String token) {
        return null;
    }
}
