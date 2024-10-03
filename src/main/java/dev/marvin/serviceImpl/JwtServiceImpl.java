package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.SecurityConstants;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.exception.ServiceException;
import dev.marvin.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;

    public JwtServiceImpl() {
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
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
                    .setIssuedAt(date)
                    .setSubject(authentication.getName())
                    .setExpiration(expirationDate)
                    .setClaims(claimsMap)
                    .signWith(secretKey)
                    .compact();

        } catch (Exception e) {
            log.error("Unexpected error occurred in generateToken method of JwtServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    public Boolean validateToken(String token) {
        log.info("Inside validateToken method of JwtServiceImpl");
        try {
            Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            log.error("Unexpected error occurred in validateToken method of JwtServiceImpl: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Map<String, Object> extractClaimsFromToken(String token) {
        log.info("Inside extractClaimsFromToken method of JwtServiceImpl");
        try {
            Claims claims = Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
            return new HashMap<>(claims);
        } catch (JwtException e) {
            log.error("Unexpected error occurred in extractClaimsFromToken method of JwtServiceImpl: {}", e.getMessage(), e);
            return Collections.emptyMap();
        }

    }
}
