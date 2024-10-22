package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.RoleEntity;
import dev.marvin.domain.RoleEnum;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.PasswordCreationRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserRegistrationRequest;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.repository.RoleRepository;
import dev.marvin.repository.UserRepository;
import dev.marvin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public ResponseDto<String> add(UserRegistrationRequest registrationRequest) {
        log.info("Inside add method of UserServiceImpl");
        try {
            UserEntity user = new UserEntity();
            user.setMobileNumber(registrationRequest.mobileNumber());

            RoleEntity role = roleRepository.findByName(RoleEnum.USER.name()).orElse(null);
            user.setRoleEntity(role);
            userRepository.save(user);
            return new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), null);

        } catch (DataIntegrityViolationException e) {
            log.info("DataIntegrityViolationException: {}", e.getMessage(), e);
            if (e.getMessage().contains("email")) {
                throw new DuplicateResourceException("email already taken");
            } else if (e.getMessage().contains("mobile_number")) {
                throw new DuplicateResourceException("mobile number already taken");
            } else {
                throw new DuplicateResourceException(e.getMessage());
            }
        } catch (Exception e) {
            log.error("unexpected error occurred in add method of UserServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseDto<String> updatePassword(PasswordCreationRequest passwordCreationRequest) {
        log.info("Inside updatePassword method of UserServiceImpl");
        try {
            // Retrieve the user by mobile number
            UserEntity user = userRepository.findByMobile(passwordCreationRequest.mobile())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            // Encode the password
            String encodedPassword = passwordEncoder.encode(passwordCreationRequest.password());

            // Update the user's password
            user.setPassword(encodedPassword);

            // Save the updated user
            userRepository.save(user);

            return new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Password created successfully");

        } catch (Exception e) {
            log.error("unexpected error occurred in add method of UserServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }
}
