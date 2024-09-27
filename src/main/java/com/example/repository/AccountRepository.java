package com.example.repository;


import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Custom method to find an account by username
    Optional<Account> findByUsername(String username);
}


