package com.koushik.expansetracker.dto;

import com.koushik.expansetracker.entity.finance.enums.Frequency;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    @NotNull(message = "Frequency is required")
    private Frequency frequency;

    private Timestamp nextRun; // optional: if null, backend can set
}
