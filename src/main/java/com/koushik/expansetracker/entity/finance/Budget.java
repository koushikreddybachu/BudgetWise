package com.koushik.expansetracker.entity.finance;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    private Long userId;
    private Long categoryId;

    private BigDecimal amountLimit;

    private LocalDate startDate;
    private LocalDate endDate;
}
