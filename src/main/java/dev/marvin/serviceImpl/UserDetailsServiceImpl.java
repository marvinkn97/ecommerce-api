package dev.marvin.serviceImpl;

import dev.marvin.domain.UserEntity;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername method of UserDetailsServiceImpl");
         UserEntity userEntity = userRepository.findByMobile(mobile)
                .orElseThrow(()-> new UsernameNotFoundException("User with given mobile number [%s] not found".formatted(mobile)));
         return new UserPrincipal(userEntity);
    }
}
