package com.koushik.expansetracker.repository.finance;

import com.koushik.expansetracker.entity.finance.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    List<Transaction> findByUserIdAndTransactionDateBetween(
            Long userId,
            Timestamp startDate,
            Timestamp endDate
    );
}
