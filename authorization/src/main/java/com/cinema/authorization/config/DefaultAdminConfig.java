package com.cinema.authorization.config;

import com.cinema.authorization.model.entity.UserAccount;
import com.cinema.authorization.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultAdminConfig {

    @Bean
    ApplicationRunner createDefaultAdmin(
            UserAccountRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.security.default-admin.username}") String username,
            @Value("${app.security.default-admin.password}") String password
    ) {
        return arguments -> {
            if (repository.existsByUsername(username)) {
                return;
            }

            UserAccount admin = new UserAccount();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode(password));
            admin.setRole("ADMIN");
            admin.setDisabled(false);
            repository.save(admin);
        };
    }
}
