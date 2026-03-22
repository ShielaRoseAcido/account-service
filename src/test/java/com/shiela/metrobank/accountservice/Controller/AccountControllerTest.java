package com.shiela.metrobank.accountservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiela.metrobank.accountservice.dto.request.CreateAccountRequest;
import com.shiela.metrobank.accountservice.dto.response.AccountInfoResponse;
import com.shiela.metrobank.accountservice.dto.response.CreateAccountResponse;
import com.shiela.metrobank.accountservice.dto.response.CustomerInquiryResponse;
import com.shiela.metrobank.accountservice.entity.AccountType;
import com.shiela.metrobank.accountservice.exception.CustomerNotFoundException;
import com.shiela.metrobank.accountservice.exception.GlobalExceptionHandler;
import com.shiela.metrobank.accountservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import(GlobalExceptionHandler.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    private final BasicJsonTester json = new BasicJsonTester(getClass());

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setCustomerName("Test");
        request.setCustomerMobile("09081234567");
        request.setCustomerEmail("test12345@gmail.com");
        request.setAddress1("test");
        request.setAddress2("test");
        request.setAccountType(AccountType.S);

        CreateAccountResponse response = CreateAccountResponse.builder()
                .customerNumber(12345678L)
                .transactionStatusCode(201)
                .transactionStatusDescription("Customer account created")
                .build();

        when(accountService.createAccount(any(CreateAccountRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerNumber").value(12345678L))
                .andExpect(jsonPath("$.transactionStatusCode").value(201))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer account created"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        String invalidRequest = "{\n" +
                "  \"customerName\": \"Test\",\n" +
                "  \"customerMobile\": \"09081234567\",\n" +
                "  \"customerEmail\": \"invalid-email\",\n" +
                "  \"address1\": \"test\",\n" +
                "  \"address2\": \"test\",\n" +
                "  \"accountType\": \"S\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionStatusCode").value(400));
    }

    @Test
    void shouldReturnCustomerInquirySuccessfully() throws Exception {
        CustomerInquiryResponse response = CustomerInquiryResponse.builder()
                .customerNumber(12345678L)
                .customerName("Test")
                .customerMobile("09081234567")
                .customerEmail("test12345@gmail.com")
                .address1("test")
                .address2("test")
                .savings(Collections.singletonList(
                        AccountInfoResponse.builder()
                                .accountNumber(10001L)
                                .accountType("Savings")
                                .availableBalance(BigDecimal.ZERO)
                                .build()
                ))
                .transactionStatusCode(302)
                .transactionStatusDescription("Customer Account found")
                .build();

        when(accountService.getCustomerByNumber(12345678L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/account/12345678"))
                .andExpect(status().isFound())
                .andExpect(jsonPath("$.customerNumber").value(12345678L))
                .andExpect(jsonPath("$.transactionStatusCode").value(302))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer Account found"));
    }

    @Test
    void shouldReturnUnauthorizedWhenCustomerNotFound() throws Exception {
        when(accountService.getCustomerByNumber(99999999L))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/v1/account/99999999"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.transactionStatusCode").value(401))
                .andExpect(jsonPath("$.transactionStatusDescription").value("Customer not found"));
    }
}