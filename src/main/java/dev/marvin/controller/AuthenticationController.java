package dev.marvin.controller;

import dev.marvin.dto.AuthenticationRequest;
import dev.marvin.dto.AuthenticationResponse;
import dev.marvin.service.JwtService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        log.info("Inside authenticate method of AuthenticationController");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.username(), authenticationRequest.password()));
        if (!authentication.isAuthenticated() || ObjectUtils.isEmpty(authentication)) {
            throw new BadCredentialsException(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
