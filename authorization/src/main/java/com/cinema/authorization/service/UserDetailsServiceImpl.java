package com.cinema.authorization.service;

import com.cinema.authorization.model.entity.UserAccount;
import com.cinema.authorization.repository.UserAccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserAccountRepository accountRepository;

    public UserDetailsServiceImpl(UserAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserAccount account = accountRepository.findByUsername(username);
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .disabled(account.getDisabled())
                .build();
    }
}
