package dev.marvin.serviceImpl;

import dev.marvin.domain.SecurityConstants;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;

    public JwtServiceImpl() {
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getEncoded());
    }

    @Override
    public String generateToken(Authentication authentication) {
        log.info("Inside generateToken method of JwtServiceImpl");
        try {
            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            }

            Date date = SecurityConstants.ISSUED_AT;
            log.info("iat: {}", date);

            Date expirationDate = SecurityConstants.EXPIRES_AT;
            log.info("exp: {}", expirationDate);

            Map<String, Object> claimsMap = new HashMap<>();

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            claimsMap.put("emailId", principal.getUsername());
            claimsMap.put("roleName", principal.userEntity().getRoleEntity().getRoleName());

            return Jwts.builder()
                    .issuedAt(date)
                    .subject(authentication.getName())
                    .expiration(expirationDate)
                    .claims(claimsMap)
                    .signWith(secretKey)
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

    @Override
    public Map<String, Object> extractClaimsFromToken(String token) {
        return null;
    }
}
