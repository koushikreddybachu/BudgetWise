package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.RecurringTransaction;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.enums.Frequency;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.RecurringTransactionRepository;
import com.koushik.expansetracker.service.finance.interfaces.RecurringTransactionServiceInterface;
import com.koushik.expansetracker.service.finance.interfaces.TransactionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService implements RecurringTransactionServiceInterface {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionServiceInterface transactionServiceInterface;

    @Override
    public RecurringTransaction createRule(RecurringTransaction rule) {
        return recurringTransactionRepository.save(rule);
    }

    @Override
    public List<RecurringTransaction> getRulesForUser(Long userId) {
        return recurringTransactionRepository.findByUserId(userId);
    }

    @Override
    public RecurringTransaction getRuleById(Long recurringId) {
        return recurringTransactionRepository.findById(recurringId)
                .orElseThrow(() -> new RuntimeException("Recurring rule not found with id: " + recurringId));
    }

    @Override
    public RecurringTransaction updateRule(Long recurringId, RecurringTransaction updated) {
        RecurringTransaction existing = getRuleById(recurringId);
        existing.setAccountId(updated.getAccountId());
        existing.setCategoryId(updated.getCategoryId());
        existing.setAmount(updated.getAmount());
        existing.setFrequency(updated.getFrequency());
        existing.setNextRun(updated.getNextRun());
        return recurringTransactionRepository.save(existing);
    }

    @Override
    public void deleteRule(Long recurringId) {
        recurringTransactionRepository.deleteById(recurringId);
    }

    @Override
    public void processDueRecurringTransactions() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        List<RecurringTransaction> dueRules = recurringTransactionRepository.findByNextRunBefore(now);

        for (RecurringTransaction rule : dueRules) {
            // For now, treat all recurring transactions as EXPENSE
            Transaction t = Transaction.builder()
                    .userId(rule.getUserId())
                    .accountId(rule.getAccountId())
                    .categoryId(rule.getCategoryId())
                    .amount(rule.getAmount())
                    .type(TransactionType.EXPENSE)
                    .transactionDate(now)
                    .build();

            transactionServiceInterface.createTransaction(t, null);

            // update nextRun based on frequency
            LocalDateTime next = rule.getNextRun() != null
                    ? rule.getNextRun().toLocalDateTime()
                    : LocalDateTime.now();

            if (rule.getFrequency() == Frequency.DAILY) {
                next = next.plusDays(1);
            } else if (rule.getFrequency() == Frequency.WEEKLY) {
                next = next.plusWeeks(1);
            } else if (rule.getFrequency() == Frequency.MONTHLY) {
                next = next.plusMonths(1);
            } else if (rule.getFrequency() == Frequency.YEARLY) {
                next = next.plusYears(1);
            }

            rule.setNextRun(Timestamp.valueOf(next));
            recurringTransactionRepository.save(rule);
        }
    }
}
