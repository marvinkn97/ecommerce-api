package dev.marvin.serviceImpl;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.UserRegistrationRequest;
import dev.marvin.repository.UserRepository;
import dev.marvin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void add(UserRegistrationRequest registrationRequest) {
        log.info("Inside add method of UserServiceImpl");
        try {
            UserEntity user = new UserEntity();
            user.setFullName(registrationRequest.name());
            user.setEmail(registrationRequest.email());
            user.setPassword(passwordEncoder.encode(registrationRequest.password()));
            userRepository.save(user);

        } catch (Exception e) {
            log.error("unexpected error occurred in add method of UserServiceImpl: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }


    }
}
