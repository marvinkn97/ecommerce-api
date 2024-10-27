package dev.marvin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public record UserProfileRequest(
        String name,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        Date dateOfBirth,
        boolean termsAndConditions
         ) {
}
