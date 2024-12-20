package dev.marvin.shared;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ResponseDto", description = "Generic response DTO used for wrapping API responses with status and payload.")
public record ResponseDto<T>(
        @Schema(
                description = "The status of the API response (e.g., success, error).",
                example = "OK"
        )
        String status,

        @Schema(
                description = "The actual response data or payload of any type (can be any object).",
                example = "{\"message\": \"Operation completed successfully\"}"
        )
        T payload

) {
}
