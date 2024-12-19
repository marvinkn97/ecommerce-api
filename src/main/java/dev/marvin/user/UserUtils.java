package dev.marvin.utils;

import dev.marvin.domain.UserEntity;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;

    public UserEntity getUser(String mobile) {
        return userRepository.findByMobile(mobile)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstants.USER_NOT_FOUND));
    }
}
