package com.cinema.authorization;

import com.cinema.authorization.model.entity.UserAccount;
import com.cinema.authorization.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AuthorizationApplicationTests {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void createsDefaultAdmin() {
        UserAccount admin = repository.findByUsername("admin").orElseThrow();

        assertThat(admin.getRole()).isEqualTo("ADMIN");
        assertThat(admin.getDisabled()).isFalse();
        assertThat(passwordEncoder.matches("admin12345", admin.getPassword())).isTrue();
    }

}
