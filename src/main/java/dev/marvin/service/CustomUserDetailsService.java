package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final UserUtils userUtils;

    @Override
    public UserDetails loadUserByUsername(String mobile) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername method of UserDetailsServiceImpl");
        UserEntity userEntity = userUtils.getUser(mobile);
        return new UserPrincipal(userEntity);
    }
}
