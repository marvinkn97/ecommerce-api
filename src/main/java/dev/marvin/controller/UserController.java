package dev.marvin.controller;

import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserRegistrationRequest;
import dev.marvin.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Category Resource", description = "CRUD Operations for User Management")
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    @Operation(summary = "create", description = "create a new user", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Register with email send OTP"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Failed to register")})
    public ResponseEntity<ResponseDto<String>> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        log.info("Inside add method of UserController");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(registrationRequest));
    }



//    @PostMapping("/verify-otp")
//    public ResponseEntity<ResponseDto<String>> verifyOtp(@RequestBody OtpVerificationRequest otpRequest) {
//        return ResponseEntity.ok(userService.verifyOtp(otpRequest));
//    }


//    @PostMapping("/create-password")
//    public ResponseEntity<ResponseDto<String>> createPassword(@Valid @RequestBody PasswordCreationRequest passwordRequest) {
//        return ResponseEntity.ok(userService.createPassword(passwordRequest));
//    }

//    @PostMapping("/complete-profile")
//    public ResponseEntity<ResponseDto<String>> completeProfile(@Valid @RequestBody UserProfileRequest profileRequest) {
//        return ResponseEntity.ok(userService.completeProfile(profileRequest));
//    }

//    @PostMapping("/verify-mobile-otp")
//    public ResponseEntity<ResponseDto<String>> verifyMobileOtp(@RequestBody MobileOtpVerificationRequest otpRequest) {
//        return ResponseEntity.ok(userService.verifyMobileOtp(otpRequest));
//    }

}
