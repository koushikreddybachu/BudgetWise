package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {

    @NotBlank(message = "Account name is required")
    private String accountName;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    // Optional: initial balance
    private BigDecimal balance;

    @NotBlank(message = "Currency is required")
    private String currency;
}
