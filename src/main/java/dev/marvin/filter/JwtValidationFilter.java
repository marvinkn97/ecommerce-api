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
import java.time.LocalDateTime;

//@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(authHeader)){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.getReasonPhrase());
            errorResponse.setMessage("Invalid Token");
            errorResponse.setTimestamp(LocalDateTime.now());

            String error = new ObjectMapper().writeValueAsString(errorResponse);
            response.getWriter().println(error);
            return;
        }
        //if(authHeader.startsWith("Bearer"))

    }
}
