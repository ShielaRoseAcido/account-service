package com.shiela.metrobank.accountservice.dto.request;

import com.shiela.metrobank.accountservice.entity.AccountType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateAccountRequest {

    @NotBlank(message = "Customer name is required field")
    @Size(max = 50)
    private String customerName;

    @NotBlank(message = "Customer mobile is required field")
    @Size(max = 20)
    private String customerMobile;

    @NotBlank(message = "Email is required field")
    @Email(message = "Invalid email format")
    @Size(max = 50)
    private String customerEmail;

    @NotBlank(message = "Address1 is required field")
    @Size(max = 100)
    private String address1;

    @Size(max = 100)
    private String address2;

    @NotNull(message = "Account type is required field")
    private AccountType accountType;
}