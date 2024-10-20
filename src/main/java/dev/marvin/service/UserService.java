package dev.marvin.service;

import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserRegistrationRequest;

public interface UserService {
   ResponseDto<String> add(UserRegistrationRequest registrationRequest);

}
