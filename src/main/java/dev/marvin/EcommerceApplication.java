package dev.marvin;

import dev.marvin.user.RoleEnum;
import dev.marvin.user.UserEntity;
import dev.marvin.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcommerceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(EcommerceApplication.class)
                .profiles("dev")
                .build()
                .run(args);
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner runner(PasswordEncoder passwordEncoder, UserRepository userRepository) {
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
