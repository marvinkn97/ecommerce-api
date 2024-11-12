package dev.marvin.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvin.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.info("Inside handle method of CustomAccessDeniedHandler");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.getReasonPhrase(), "You do not have the required permissions to access this resource.");
        response.getOutputStream().print(objectMapper.writeValueAsString(errorResponse));
    }
}
