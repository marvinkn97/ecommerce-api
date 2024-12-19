package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.auth.PasswordChangeRequest;
import dev.marvin.auth.PasswordCreationRequest;
import dev.marvin.dto.UserProfileRequest;
import dev.marvin.dto.UserProfileUpdateRequest;

public interface IUserService {
    void registerMobile(String mobileNumber);

    void setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

    void completeUserProfile(UserProfileRequest userProfileRequest);

    void changePassword(UserEntity userEntity, PasswordChangeRequest passwordChangeRequest);

    void updateProfile(UserEntity userEntity, UserProfileUpdateRequest userProfileUpdateRequest);

}
