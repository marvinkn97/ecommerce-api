package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.PasswordChangeRequest;
import dev.marvin.dto.PasswordCreationRequest;
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
