package dev.marvin.controller;

import dev.marvin.dto.PasswordChangeRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserProfileRequest;
import dev.marvin.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
@Tag(name = "User Resource", description = "CRUD operations for user management")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<String>> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest, Principal principal){
        log.info("Inside changePassword method of UserController");
        log.info("principal: {}", principal);
        userService.changePassword(principal.getName(), passwordChangeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), "Password changed successfully"));
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDto<String>> updateProfile(Principal principal, UserProfileRequest userProfileRequest){
        return null;
    }

}
