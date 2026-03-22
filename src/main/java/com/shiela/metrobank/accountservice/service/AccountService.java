package com.shiela.metrobank.accountservice.service;

import com.shiela.metrobank.accountservice.dto.request.CreateAccountRequest;
import com.shiela.metrobank.accountservice.dto.response.CreateAccountResponse;
import com.shiela.metrobank.accountservice.dto.response.CustomerInquiryResponse;

public interface AccountService {
    CreateAccountResponse createAccount(CreateAccountRequest request);
    CustomerInquiryResponse getCustomerByNumber(Long customerNumber);
}