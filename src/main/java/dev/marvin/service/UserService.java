package dev.marvin.service;

import dev.marvin.dto.*;

import java.security.Principal;

public interface UserService {
    void registerMobile(String mobileNumber);

    ResponseDto<String> setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

    void completeUserProfile(UserProfileRequest userProfileRequest);

    void changePassword(Principal principal, PasswordChangeRequest passwordChangeRequest);

    void updateProfile(Principal principal, UserProfileUpdateRequest userProfileUpdateRequest);

}
