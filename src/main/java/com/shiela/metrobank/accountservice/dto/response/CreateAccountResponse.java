package com.shiela.metrobank.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAccountResponse {
    private Long customerNumber;
    private int transactionStatusCode;
    private String transactionStatusDescription;
}