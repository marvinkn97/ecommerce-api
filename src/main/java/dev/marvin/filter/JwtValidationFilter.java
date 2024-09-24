package dev.marvin.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvin.exception.ErrorResponse;
import dev.marvin.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader)){
            ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(Clock.systemDefaultZone()), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Invalid Token");
            String error = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().print(error);
            return;
        }
        //if(authHeader.startsWith("Bearer"))

    }
}
