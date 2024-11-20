package dev.marvin.filter;

import dev.marvin.domain.UserEntity;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.UserRepository;
import dev.marvin.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtValidationFilter invoked");
        String token = jwtUtils.getTokenFromHeader(request);

        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(token) && Boolean.TRUE.equals(jwtUtils.validateToken(token))) {
            Map<String, Object> claims = jwtUtils.extractClaimsFromToken(token);
            log.info("claims: {}", claims);
            String mobile = claims.get("sub").toString();
            UserEntity user = userRepository.findByMobile(mobile)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with given mobile %s".formatted(mobile)));
            UserDetails userDetails = new UserPrincipal(user);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetails(request));
            log.info("usernamePasswordAuthenticationToken: {}", usernamePasswordAuthenticationToken);

            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            filterChain.doFilter(request, response);
        }
    }
}
