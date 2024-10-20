package dev.marvin.controller;

import dev.marvin.dto.AuthenticationRequest;
import dev.marvin.dto.AuthenticationResponse;
import dev.marvin.dto.PreAuthRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.utils.JwtUtils;
import dev.marvin.utils.OtpUtils;
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

    @PostMapping("/pre")
    public ResponseEntity<ResponseDto<Object>> preAuthentication(@Valid @RequestBody PreAuthRequest preAuthRequest) {
        if (!emailOrMobileRequest.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseDto<>(HttpStatus.BAD_REQUEST.getReasonPhrase(), "Invalid request provide either email or mobile"));
        }


        otpUtils.generateAndSendOtp(emailOrMobileRequest.email());
        new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "OTP sent successfully");
        return null;
    }

    @PostMapping
    public ResponseEntity<ResponseDto<Object>> authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Inside authenticate method of AuthenticationController");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.email(),
                authenticationRequest.password()));
        if (!authentication.isAuthenticated() || ObjectUtils.isEmpty(authentication)) {
            throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        String token = jwtUtils.generateToken(authentication);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), new AuthenticationResponse(token)));
    }
}
