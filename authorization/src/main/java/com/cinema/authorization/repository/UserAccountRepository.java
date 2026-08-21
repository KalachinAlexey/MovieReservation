package com.cinema.authorization.repository;

import com.cinema.authorization.model.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    UserAccount findByUsername(String username);
    boolean existsByUsername(String username);
}
