package dev.marvin.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private Integer statusCode;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
