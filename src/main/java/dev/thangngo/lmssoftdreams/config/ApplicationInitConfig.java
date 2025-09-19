package dev.thangngo.travelmate.config;

import dev.thangngo.travelmate.entities.User;
import dev.thangngo.travelmate.enums.UserRole;
import dev.thangngo.travelmate.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    private PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_EMAIL = "ngothang.learn@gmail.com";

    @NonFinal
    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("ApplicationRunner started");
        return args -> {
            if (userRepository.findByEmail(ADMIN_USER_EMAIL).isEmpty()) {
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail(ADMIN_USER_EMAIL);
                admin.setPasswordHash(passwordEncoder.encode(adminPassword));
                admin.setRole(UserRole.ADMIN);
                admin.setPoints(10000000L);
                userRepository.save(admin);
            }
            log.info("Application initialization completed .....");
        };

    }


}
