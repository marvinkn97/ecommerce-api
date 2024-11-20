package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.*;

public interface UserService {
    void registerMobile(String mobileNumber);

    ResponseDto<String> setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

    void completeUserProfile(UserProfileRequest userProfileRequest);

    void changePassword(UserEntity userEntity, PasswordChangeRequest passwordChangeRequest);

    void updateProfile(UserEntity userEntity, UserProfileUpdateRequest userProfileUpdateRequest);

}
