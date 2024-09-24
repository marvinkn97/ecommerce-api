package dev.marvin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Object> handleDuplicateResourceException(DuplicateResourceException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(buildErrorResponseObject(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }

    private ErrorResponse buildErrorResponseObject(HttpStatus status, String message) {
        return new ErrorResponse(LocalDateTime.now(Clock.systemDefaultZone()), status.value(), status.getReasonPhrase(), message);
    }
}
