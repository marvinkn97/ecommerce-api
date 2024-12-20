package dev.marvin;

import dev.marvin.user.RoleEnum;
import dev.marvin.user.UserEntity;
import dev.marvin.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EcommerceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository){
        return args -> {
            UserEntity admin = new UserEntity();
            admin.setMobileNumber("254796196179");
            admin.setFullName("Admin");
            admin.setPassword(passwordEncoder.encode("password"));
            admin.setRoleEnum(RoleEnum.ROLE_ADMIN);
            userRepository.save(admin);

            UserEntity user = new UserEntity();
            user.setMobileNumber("254792865297");
            user.setFullName("Marvin");
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoleEnum(RoleEnum.ROLE_USER);
            userRepository.save(user);
        };
    }
}
