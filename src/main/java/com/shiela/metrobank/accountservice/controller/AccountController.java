package com.shiela.metrobank.accountservice.controller;

import com.shiela.metrobank.accountservice.dto.request.CreateAccountRequest;
import com.shiela.metrobank.accountservice.dto.response.CreateAccountResponse;
import com.shiela.metrobank.accountservice.dto.response.CustomerInquiryResponse;
import com.shiela.metrobank.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        CreateAccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{customerNumber}")
    public ResponseEntity<CustomerInquiryResponse> getCustomer(@PathVariable Long customerNumber) {
        CustomerInquiryResponse response = accountService.getCustomerByNumber(customerNumber);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }
}