package dev.marvin.controller;

import dev.marvin.utils.MessageConstants;
import dev.marvin.dto.*;
import dev.marvin.service.IUserService;
import dev.marvin.utils.JwtUtils;
import dev.marvin.utils.OtpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.prefix}/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication Resource", description = "User Authentication Operation")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final OtpUtils otpUtils;
    private final IUserService userService;

    @PostMapping("/verify-user")
    @Operation(summary = "Verify user", description = "Checks if a user is registered by their mobile number. If registered, prompts login; otherwise, sends an OTP for registration.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful response indicating next steps"), @ApiResponse(responseCode = "400", description = "Invalid mobile number or request format"), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> verifyUser(@Valid @RequestBody PreAuthRequest preAuthRequest) {
        log.info("Inside verifyUser method of AuthenticationController");
        if (Boolean.TRUE.equals(userService.isUserRegistered(preAuthRequest.mobile()))) {
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PROCEED_TO_LOGIN));
        } else {
            otpUtils.generateAndSendOtp(preAuthRequest);
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.OTP_SENT));
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP sent to the user's mobile number for authentication purposes.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OTP verified successfully. Proceed to the password creation screen."), @ApiResponse(responseCode = "400", description = "Invalid OTP format or mobile number."), @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")})
    public ResponseEntity<ResponseDto<Object>> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        log.info("Inside verifyOtp method of AuthenticationController");
        otpUtils.verifyOtp(otpVerificationRequest);
        userService.registerMobile(otpVerificationRequest.mobile());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PROCEED_TO_PASSWORD_CREATION));
    }

    @PostMapping("/create-password")
    @Operation(summary = "Create Password", description = "Creates a secure password for the user. Password must be at least 8 characters long.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Password created successfully", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid password format or request data."), @ApiResponse(responseCode = "500", description = "Internal server error.")})
    public ResponseEntity<ResponseDto<String>> createPassword(@Valid @RequestBody PasswordCreationRequest passwordCreationRequest) {
        log.info("Inside createPassword method of AuthenticationController");
        userService.setPasswordForUser(passwordCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.PASSWORD_CREATED));
    }

    @PostMapping("/complete-profile")
    @Operation(summary = "Complete Profile", description = "Completes the user profile by setting up required user details.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User profile completed successfully.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<String>> completeProfile(@Valid @RequestBody UserProfileRequest userProfileRequest) {
        log.info("Inside completeProfile method of AuthenticationController");
        userService.completeUserProfile(userProfileRequest);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.ACCOUNT_CREATED));
    }


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Verifies user credentials and generates a JWT token for successful login.", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully authenticated and JWT token generated"), @ApiResponse(responseCode = "401", description = "Invalid credentials provided")})
    public ResponseEntity<ResponseDto<Object>> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Inside authenticate method of AuthenticationController");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.mobile(), authenticationRequest.password()));
        String token = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), new AuthenticationResponse(token)));
    }
}
