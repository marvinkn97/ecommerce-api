package dev.marvin;

import dev.marvin.domain.RoleEnum;
import dev.marvin.domain.UserEntity;
import dev.marvin.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

//    @Bean
    public CommandLineRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository){
        return args -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setMobileNumber("254707808236");
            userEntity.setFullName("Admin");
            userEntity.setPassword(passwordEncoder.encode("password"));
            userEntity.setRoleEnum(RoleEnum.ROLE_ADMIN);
            userRepository.save(userEntity);
        };
    }
}
