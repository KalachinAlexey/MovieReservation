package com.cinema.authorization.controller;

import com.cinema.authorization.model.dto.RequestUser;
import com.cinema.authorization.model.entity.UserAccount;
import com.cinema.authorization.repository.UserAccountRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {
    private final UserAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(UserAccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RequestUser user) {
        UserAccount account = new UserAccount();
        account.setUsername(user.username());
        account.setPassword(passwordEncoder.encode(user.password()));
        accountRepository.save(account); // check if exists (atomic)
        return ResponseEntity.ok().build();
    }

}
