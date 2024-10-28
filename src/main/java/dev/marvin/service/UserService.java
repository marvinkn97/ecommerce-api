package dev.marvin.service;

import dev.marvin.dto.PasswordCreationRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserProfileRequest;

public interface UserService {
    void registerMobile(String mobileNumber);

    ResponseDto<String> setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

    void completeUserProfile(UserProfileRequest userProfileRequest);

}
