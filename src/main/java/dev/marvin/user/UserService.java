package dev.marvin.user;

import dev.marvin.auth.PasswordChangeRequest;
import dev.marvin.auth.PasswordCreationRequest;

public interface UserService {
    void registerMobile(String mobileNumber);

    void setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

    void completeUserProfile(UserProfileRequest userProfileRequest);

    void changePassword(UserEntity userEntity, PasswordChangeRequest passwordChangeRequest);

    void updateProfile(UserEntity userEntity, UserProfileUpdateRequest userProfileUpdateRequest);

}
