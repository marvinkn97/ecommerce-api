package dev.marvin.controller;

import dev.marvin.dto.*;
import dev.marvin.service.UserService;
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
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication Resource", description = "User Authentication Operation")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final OtpUtils otpUtils;
    private final UserService userService;

    @PostMapping("/verify-user")
    @Operation(summary = "Verify user", description = "Checks if a user is registered by their mobile number. If registered, prompts login; otherwise, sends an OTP for registration.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful response indicating next steps", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid mobile number or request format", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<Object>> verifyUser(@Valid @RequestBody PreAuthRequest preAuthRequest) {
        log.info("Inside verifyUser method of AuthenticationController");
        if (Boolean.TRUE.equals(userService.isUserRegistered(preAuthRequest.mobile()))) {
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "Proceed to login screen"));
        } else {
            otpUtils.generateAndSendOtp(preAuthRequest);
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "OTP sent successfully. Proceed to otp verification screen"));
        }
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verifies the OTP sent to the user's mobile number for authentication purposes.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "OTP verified successfully. Proceed to the password creation screen.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid OTP format or mobile number.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<Object>> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        log.info("Inside verifyOtp method of AuthenticationController");
        otpUtils.verifyOtp(otpVerificationRequest);
        userService.registerMobile(otpVerificationRequest.mobile());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "OTP is valid. Proceed to password creation screen"));
    }

    @PostMapping("/create-password")
    @Operation(summary = "Create Password", description = "Creates a secure password for the user. Password must be at least 8 characters long.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Password created successfully.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid password format or request data.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<String>> createPassword(@Valid @RequestBody PasswordCreationRequest passwordCreationRequest) {
        log.info("Inside createPassword method of AuthenticationController");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.setPasswordForUser(passwordCreationRequest));
    }

    @PostMapping("/complete-profile")
    @Operation(summary = "Complete Profile", description = "Completes the user profile by setting up required user details.", method = "POST")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "User profile completed successfully.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<String>> completeProfile(@Valid @RequestBody UserProfileRequest userProfileRequest) {
        log.info("Inside completeProfile method of AuthenticationController");
        userService.completeUserProfile(userProfileRequest);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Your Account has been created"));
    }


    @PostMapping("/login")
    @Operation(summary = "User login", description = "Verifies user credentials and generates a JWT token for successful login.", method = "POST")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User successfully authenticated and JWT token generated.", content = @Content(schema = @Schema(implementation = ResponseDto.class))), @ApiResponse(responseCode = "401", description = "Invalid credentials provided.", content = @Content(schema = @Schema(implementation = ResponseDto.class)))})
    public ResponseEntity<ResponseDto<Object>> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Inside authenticate method of AuthenticationController");
        log.info("request: {}", authenticationRequest);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.mobile(), authenticationRequest.password()));
        String token = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), new AuthenticationResponse(token)));
    }
}
