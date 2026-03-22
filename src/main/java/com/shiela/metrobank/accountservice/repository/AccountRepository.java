package com.shiela.metrobank.accountservice.repository;

import com.shiela.metrobank.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(Long accountNumber);
}