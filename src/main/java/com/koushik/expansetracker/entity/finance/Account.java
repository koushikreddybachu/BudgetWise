package com.koushik.expansetracker.entity.finance;

import com.koushik.expansetracker.entity.finance.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    private Long userId; // FK → users.userId

    private String accountName;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;

    private String currency;

    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
}
