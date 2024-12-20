package dev.marvin.auth;


import dev.marvin.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
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
        Integer userId = userPrincipal.userEntity().getId();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        claims.put("userId", userId);

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
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
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

    public Claims extractClaimsFromToken(String token) {
        log.info("Inside extractClaimsFromToken method of JwtUtils");
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token) {
        log.info("Inside validateToken method of JwtUtils");
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return claimsJws.getBody().getIssuer().equals(ISSUER);
    }
}
