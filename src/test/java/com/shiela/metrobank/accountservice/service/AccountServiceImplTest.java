package com.shiela.metrobank.accountservice.service;

import com.shiela.metrobank.accountservice.dto.request.CreateAccountRequest;
import com.shiela.metrobank.accountservice.dto.response.CreateAccountResponse;
import com.shiela.metrobank.accountservice.dto.response.CustomerInquiryResponse;
import com.shiela.metrobank.accountservice.entity.Account;
import com.shiela.metrobank.accountservice.entity.AccountType;
import com.shiela.metrobank.accountservice.entity.Customer;
import com.shiela.metrobank.accountservice.exception.CustomerNotFoundException;
import com.shiela.metrobank.accountservice.repository.AccountRepository;
import com.shiela.metrobank.accountservice.repository.CustomerRepository;
import com.shiela.metrobank.accountservice.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void shouldCreateCustomerAccountSuccessfully() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("09081234567");
        request.setCustomerEmail("test12345@gmail.com");
        request.setAddress1("test");
        request.setAddress2("test");
        request.setAccountType(AccountType.S);

        when(customerRepository.existsByCustomerEmail("test12345@gmail.com")).thenReturn(false);
        when(customerRepository.existsByCustomerNumber(anyLong())).thenReturn(false);
        when(accountRepository.existsByAccountNumber(anyLong())).thenReturn(false);
        when(customerRepository.save(ArgumentMatchers.any(Customer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreateAccountResponse response = accountService.createAccount(request);

        assertNotNull(response.getCustomerNumber());
        assertEquals(201, response.getTransactionStatusCode());
        assertEquals("Customer account created", response.getTransactionStatusDescription());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerEmail("test12345@gmail.com");

        when(customerRepository.existsByCustomerEmail("test12345@gmail.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(request));

        assertEquals("Email already exists", ex.getMessage());
    }

    @Test
    void shouldReturnCustomerInquirySuccessfully() {
        Customer customer = Customer.builder()
                .id(1L)
                .customerNumber(12345678L)
                .customerName("Test")
                .customerMobile("09081234567")
                .customerEmail("test12345@gmail.com")
                .address1("test")
                .address2("test")
                .build();

        Account account = Account.builder()
                .id(1L)
                .accountNumber(10001L)
                .accountType(AccountType.S)
                .availableBalance(BigDecimal.ZERO)
                .customer(customer)
                .build();

        customer.setAccounts(Collections.singletonList(account));

        when(customerRepository.findByCustomerNumber(12345678L)).thenReturn(Optional.of(customer));

        CustomerInquiryResponse response = accountService.getCustomerByNumber(12345678L);

        assertEquals(Long.valueOf(12345678L), response.getCustomerNumber());
        assertEquals("Test", response.getCustomerName());
        assertEquals(1, response.getSavings().size());
        assertEquals(302, response.getTransactionStatusCode());
        assertEquals("Customer Account found", response.getTransactionStatusDescription());
    }

    @Test
    void shouldThrowCustomerNotFoundWhenCustomerNumberDoesNotExist() {
        when(customerRepository.findByCustomerNumber(99999999L)).thenReturn(Optional.empty());

        CustomerNotFoundException ex = assertThrows(CustomerNotFoundException.class,
                () -> accountService.getCustomerByNumber(99999999L));

        assertEquals("Customer not found", ex.getMessage());
    }
}