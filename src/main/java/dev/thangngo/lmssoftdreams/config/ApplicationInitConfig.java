package dev.thangngo.lmssoftdreams.config;

import dev.thangngo.lmssoftdreams.entities.User;
import dev.thangngo.lmssoftdreams.enums.UserRole;
import dev.thangngo.lmssoftdreams.repositories.users.UserRepository;
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
    static final String ADMIN_USER_NAME = "ngothang";

    @NonFinal
    @Value("${admin.password}")
    private String adminPassword;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("ApplicationRunner started");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                User admin = new User();
                admin.setUsername(ADMIN_USER_NAME);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(UserRole.ADMIN);
                admin.setFullName("Toi yeu em");
                userRepository.save(admin);
            }
            log.info("Application initialization completed .....");
        };

    }


}
