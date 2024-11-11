package dev.marvin.controller;

import dev.marvin.dto.*;
import dev.marvin.service.UserService;
import dev.marvin.utils.JwtUtils;
import dev.marvin.utils.OtpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
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
    @Operation(summary = "Verify user", description = "Verify user by mobile number", method = "POST")
    public ResponseEntity<ResponseDto<Object>> verifyUser(@Valid @RequestBody PreAuthRequest preAuthRequest) {
        log.info("Inside verifyUser method of AuthenticationController");
        if (Boolean.TRUE.equals(userService.isUserRegistered(preAuthRequest.mobile()))) {
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "Proceed to login screen"));
        } else {
            otpUtils.generateAndSendOtp(preAuthRequest);
            return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "OTP sent successfully. Proceed to otp verification screen"));
        }
    }

    @PostMapping("/verify-otp")  // check into this method
    public ResponseEntity<ResponseDto<Object>> verifyOtp(@Valid @RequestBody OtpVerificationRequest otpVerificationRequest) {
        log.info("Inside verifyOtp method of AuthenticationController");
        otpUtils.verifyOtp(otpVerificationRequest);
        userService.registerMobile(otpVerificationRequest.mobile());
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "OTP is valid. Proceed to password creation screen"));
    }

    @PostMapping("/create-password")
    public ResponseEntity<ResponseDto<String>> createPassword(@Valid @RequestBody PasswordCreationRequest passwordCreationRequest) {
        log.info("Inside createPassword method of AuthenticationController");
        userService.setPasswordForUser(passwordCreationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.setPasswordForUser(passwordCreationRequest));
    }

    @PostMapping("/complete-profile")
    public ResponseEntity<ResponseDto<String>> completeProfile(@Valid @RequestBody UserProfileRequest userProfileRequest){
        log.info("Inside completeProfile method of AuthenticationController");
        userService.completeUserProfile(userProfileRequest);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Your Account has been created"));
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Object>> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        log.info("Inside authenticate method of AuthenticationController");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.mobile(),
                authenticationRequest.password()));
        if (!authentication.isAuthenticated() || ObjectUtils.isEmpty(authentication)) {
            throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        String token = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), new AuthenticationResponse(token)));
    }


}
