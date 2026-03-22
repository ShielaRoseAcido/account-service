package com.shiela.metrobank.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountInfoResponse {
    private Long accountNumber;
    private String accountType;
    private BigDecimal availableBalance;
}