package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.RoleEntity;
import dev.marvin.domain.RoleEnum;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.PasswordCreationRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.dto.UserProfileRequest;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void registerMobile(String mobile) {
        log.info("Inside add method of UserServiceImpl");
        try {
            UserEntity user = new UserEntity();
            user.setMobileNumber(mobile);

            RoleEntity role = roleRepository.findByName(RoleEnum.USER.name()).orElse(null);
            user.setRoleEntity(role);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.info("DataIntegrityViolationException: {}", e.getMessage(), e);
            if (e.getMessage().contains("mobile_number")) {
                throw new DuplicateResourceException("mobile number already taken");
            } else {
                throw new DuplicateResourceException(e.getMessage());
            }
        } catch (Exception e) {
            log.error("Unexpected error occurred in registerMobile method of UserServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseDto<String> setPasswordForUser(PasswordCreationRequest passwordCreationRequest) {
        log.info("Inside setPasswordForUser method of UserServiceImpl");
        try {
            UserEntity user = userRepository.findByMobile(passwordCreationRequest.mobile())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with given mobile %s".formatted(passwordCreationRequest.mobile())));

            if (!passwordCreationRequest.passwordMatch()) {
                throw new RequestValidationException("Password and confirm password do not match.");
            }

            String encodedPassword = passwordEncoder.encode(passwordCreationRequest.password());
            user.setPassword(encodedPassword);
            userRepository.save(user);
            return new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), "Password set successfully for user with mobile %s".formatted(passwordCreationRequest.mobile()));

        } catch (ResourceNotFoundException | RequestValidationException e) {
            log.error("Error in setPasswordForUser: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in setPasswordForUser method of UserServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }

    @Override
    public Boolean isUserRegistered(String mobileNumber) {
        log.info("Inside isUserRegistered method of UserServiceImpl");
        return userRepository.findByMobile(mobileNumber).isPresent();
    }

    @Override
    public void completeUserProfile(UserProfileRequest userProfileRequest) {
        log.info("Inside completeUserProfile method of UserServiceImpl");
        try {

            UserEntity user = userRepository.findByMobile(userProfileRequest.mobile())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with given mobile %s".formatted(userProfileRequest.mobile())));

            user.setFullName(userProfileRequest.name());
            user.setDateOfBirth(userProfileRequest.dateOfBirth());
            user.setIsFullyRegistered(userProfileRequest.termsAndConditions());
            userRepository.save(user);

        } catch (ResourceNotFoundException e) {
            log.error("Error in setPasswordForUser: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in completeUserProfile method of UserServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }

    }
}
