package dev.marvin.serviceImpl;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.RoleEnum;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.*;
import dev.marvin.exception.DuplicateResourceException;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.exception.ServiceException;
import dev.marvin.repository.UserRepository;
import dev.marvin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerMobile(String mobile) {
        log.info("Inside add method of UserServiceImpl");
        try {
            UserEntity user = new UserEntity();
            user.setMobileNumber(mobile);
            user.setRoleEnum(RoleEnum.ROLE_USER);
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
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
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

    @Override
    public void changePassword(Principal principal, PasswordChangeRequest passwordChangeRequest) {
        log.info("Inside changePassword method of UserServiceImpl ");
        try {

            final String username = principal.getName();

            if (!StringUtils.hasText(username) && ObjectUtils.isEmpty(passwordChangeRequest)) {
                return;
            }

            if (!passwordChangeRequest.isNewPasswordMatching()) {
                log.warn("New password and confirmation do not match for user with mobile number: {}", username);
                throw new RequestValidationException("new password and confirm password don't match");
            }

            if (!passwordChangeRequest.isNewPasswordSameAsOld()) {
                throw new RequestValidationException("New password cannot be the same as the old password.");
            }


            UserEntity userEntity = userRepository.findByMobile(username)
                    .orElseThrow(() -> {
                        log.error("User with mobile number [{}] not found", username);
                        return new UsernameNotFoundException("User with given mobile number [%s] not found".formatted(username));
                    });

            if (!passwordEncoder.matches(passwordChangeRequest.previousPassword(), userEntity.getPassword())) {
                log.warn("Previous password does not match stored password for user with mobile number: {}", username);
                throw new RequestValidationException("previous password does not match stored password");
            }

            userEntity.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
            userRepository.save(userEntity);
            log.info("Password successfully changed for user with mobile number: {}", username);
        } catch (ResourceNotFoundException | RequestValidationException e) {
            log.error("Error in changePassword: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in changePassword method of UserServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);

        }
    }

    @Override
    public void updateProfile(Principal principal, UserProfileUpdateRequest userProfileUpdateRequest) {
        log.info("Inside updateProfile method of UserServiceImpl ");
        try {
            final String username = principal.getName();

            if (!StringUtils.hasText(username) && ObjectUtils.isEmpty(userProfileUpdateRequest)) {
                return;
            }

            UserEntity userEntity = userRepository.findByMobile(username)
                    .orElseThrow(() -> {
                        log.error("User with mobile number [{}] not found", username);
                        return new UsernameNotFoundException("User with given mobile number [%s] not found".formatted(username));
                    });

            boolean changes = false;
            String name = userProfileUpdateRequest.name();

            if (StringUtils.hasText(name) && !Objects.equals(userEntity.getFullName(), name)) {
                userEntity.setFullName(name);
                changes = true;
            }

            if (!changes) {
                log.info("No changes detected for user with mobile number: {}", username);
                throw new RequestValidationException("no data changes detected");
            }

            userRepository.save(userEntity);
            log.info("Profile successfully updated for user with mobile number: {}", username);

        } catch (ResourceNotFoundException | RequestValidationException e) {
            log.error("Error in changePassword: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error occurred in updateProfile method of UserServiceImpl: {}", e.getMessage(), e);
            throw new ServiceException(MessageConstants.UNEXPECTED_ERROR, e);
        }
    }
}
