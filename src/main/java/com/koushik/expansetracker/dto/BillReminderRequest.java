package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillReminderRequest {

    @NotBlank(message = "Bill name is required")
    private String billName;

    @NotNull(message = "Amount due is required")
    private BigDecimal amountDue;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
