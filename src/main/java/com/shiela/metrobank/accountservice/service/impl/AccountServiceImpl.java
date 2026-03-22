package com.shiela.metrobank.accountservice.service.impl;

import com.shiela.metrobank.accountservice.dto.request.CreateAccountRequest;
import com.shiela.metrobank.accountservice.dto.response.AccountInfoResponse;
import com.shiela.metrobank.accountservice.dto.response.CreateAccountResponse;
import com.shiela.metrobank.accountservice.dto.response.CustomerInquiryResponse;
import com.shiela.metrobank.accountservice.entity.Account;
import com.shiela.metrobank.accountservice.entity.AccountType;
import com.shiela.metrobank.accountservice.entity.Customer;
import com.shiela.metrobank.accountservice.exception.CustomerNotFoundException;
import com.shiela.metrobank.accountservice.repository.AccountRepository;
import com.shiela.metrobank.accountservice.repository.CustomerRepository;
import com.shiela.metrobank.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Override
    public CreateAccountResponse createAccount(CreateAccountRequest request) {

        if (customerRepository.existsByCustomerEmail(request.getCustomerEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Long customerNumber = generateUniqueCustomerNumber();
        Long accountNumber = generateUniqueAccountNumber();

        Customer customer = Customer.builder()
                .customerNumber(customerNumber)
                .customerName(request.getCustomerName())
                .customerMobile(request.getCustomerMobile())
                .customerEmail(request.getCustomerEmail())
                .address1(request.getAddress1())
                .address2(request.getAddress2())
                .build();

        customer.setAccounts(new java.util.ArrayList<>());

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .availableBalance(BigDecimal.ZERO)
                .customer(customer)
                .build();

        customer.getAccounts().add(account);
        customerRepository.save(customer);

        return CreateAccountResponse.builder()
                .customerNumber(customerNumber)
                .transactionStatusCode(201)
                .transactionStatusDescription("Customer account created")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerInquiryResponse getCustomerByNumber(Long customerNumber) {
        Customer customer = customerRepository.findByCustomerNumber(customerNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return CustomerInquiryResponse.builder()
                .customerNumber(customer.getCustomerNumber())
                .customerName(customer.getCustomerName())
                .customerMobile(customer.getCustomerMobile())
                .customerEmail(customer.getCustomerEmail())
                .address1(customer.getAddress1())
                .address2(customer.getAddress2())
                .savings(customer.getAccounts().stream()
                        .map(this::mapToAccountInfo)
                        .collect(Collectors.toList()))
                .transactionStatusCode(302)
                .transactionStatusDescription("Customer Account found")
                .build();
    }

    private AccountInfoResponse mapToAccountInfo(Account account) {
        return AccountInfoResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType() == AccountType.S ? "Savings" : "Checking")
                .availableBalance(account.getAvailableBalance())
                .build();
    }

    private Long generateUniqueCustomerNumber() {
        long value;
        do {
            value = ThreadLocalRandom.current().nextLong(10000000L, 99999999L);
        } while (customerRepository.existsByCustomerNumber(value));
        return value;
    }

    private Long generateUniqueAccountNumber() {
        long value;
        do {
            value = ThreadLocalRandom.current().nextLong(10000L, 99999L);
        } while (accountRepository.existsByAccountNumber(value));
        return value;
    }
}