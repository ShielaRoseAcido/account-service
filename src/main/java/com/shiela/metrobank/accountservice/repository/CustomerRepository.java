package com.shiela.metrobank.accountservice.repository;

import com.shiela.metrobank.accountservice.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByCustomerNumber(Long customerNumber);
    boolean existsByCustomerEmail(String customerEmail);
    boolean existsByCustomerNumber(Long customerNumber);
}