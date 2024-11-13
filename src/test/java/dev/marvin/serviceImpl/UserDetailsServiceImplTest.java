package dev.marvin.serviceImpl;

import dev.marvin.domain.UserEntity;
import dev.marvin.domain.UserPrincipal;
import dev.marvin.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserDetailsServiceImplTest {
    UserDetailsServiceImpl underTest;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.underTest = new UserDetailsServiceImpl(userRepository);

        UserEntity user = new UserEntity();
        user.setMobileNumber("254792865297");
        user.setPassword("password123");
        userRepository.save(user);

    }

    @Test
    void loadUserByUsername() {
        //given
        String mobile = "254792865297";

        // When
        UserPrincipal result = (UserPrincipal) underTest.loadUserByUsername(mobile);

        // Then
        assertNotNull(result);
        assertEquals(mobile, result.getUsername());
        assertEquals("password123", result.getPassword());
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsException() {
        String mobile = "254712345678";
        assertThrows(UsernameNotFoundException.class, () -> underTest.loadUserByUsername(mobile));
    }
}