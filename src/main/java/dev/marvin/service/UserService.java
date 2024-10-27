package dev.marvin.service;

import dev.marvin.dto.PasswordCreationRequest;
import dev.marvin.dto.ResponseDto;

public interface UserService {
    ResponseDto<String> registerMobile(String mobileNumber);

    ResponseDto<String> setPasswordForUser(PasswordCreationRequest passwordCreationRequest);

    Boolean isUserRegistered(String mobileNumber);

}
