package dev.marvin.user;

import dev.marvin.auth.PasswordChangeRequest;
import dev.marvin.shared.MessageConstants;
import dev.marvin.shared.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.prefix}/users")
@Slf4j
@Tag(name = "User Resource", description = "CRUD operations for user management")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Change user password",
            description = "Allows a user to change their password by providing the previous and new passwords."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or password mismatch"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")
    })
    public ResponseEntity<ResponseDto<String>> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        log.info("Inside changePassword method of UserController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.changePassword(userPrincipal.userEntity(), passwordChangeRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PASSWORD_CHANGED));
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Update user profile",
            description = "Updates the profile information of the authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or no data changes detected"),
            @ApiResponse(responseCode = "403", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Unexpected error occurred when processing request")
    })
    public ResponseEntity<ResponseDto<String>> updateProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        log.info("Inside updateProfile method of UserController");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        userService.updateProfile(userPrincipal.userEntity(), request);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.PROFILE_UPDATED));
    }
}
