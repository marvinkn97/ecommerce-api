package dev.marvin.exception;

import dev.marvin.shared.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException e) {
        log.info("DuplicateResourceException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(buildErrorResponseObject(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.info("ResourceNotFoundException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(buildErrorResponseObject(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Object> handleRequestValidationException(RequestValidationException e) {
        log.info("RequestValidationException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(buildErrorResponseObject(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField, // Group by field name
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList()) // Collect messages into a list
                ));
        ErrorResponse errorResponse = buildErrorResponseObject(HttpStatus.BAD_REQUEST, errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleServiceException(Exception e) {
        log.info("Exception: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.UNEXPECTED_ERROR));
    }

    private ErrorResponse buildErrorResponseObject(HttpStatus status, Object message) {
        return new ErrorResponse(LocalDateTime.now(Clock.systemDefaultZone()), status.value(), status.getReasonPhrase(), message);
    }
}
