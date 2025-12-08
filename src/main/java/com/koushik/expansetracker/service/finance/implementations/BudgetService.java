package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Budget;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.BudgetRepository;
import com.koushik.expansetracker.repository.finance.TransactionRepository;
import com.koushik.expansetracker.service.finance.interfaces.BudgetServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService implements BudgetServiceInterface {

    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public List<Budget> getBudgetsForUser(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public Budget getBudgetById(Long budgetId) {
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found with id: " + budgetId));
    }

    @Override
    public Budget updateBudget(Long budgetId, Budget updatedBudget) {
        Budget existing = getBudgetById(budgetId);
        existing.setCategoryId(updatedBudget.getCategoryId());
        existing.setAmountLimit(updatedBudget.getAmountLimit());
        existing.setStartDate(updatedBudget.getStartDate());
        existing.setEndDate(updatedBudget.getEndDate());
        return budgetRepository.save(existing);
    }

    @Override
    public void deleteBudget(Long budgetId) {
        budgetRepository.deleteById(budgetId);
    }

    @Override
    public BigDecimal calculateSpentForBudget(Long budgetId) {
        Budget budget = getBudgetById(budgetId);

        LocalDate start = budget.getStartDate();
        LocalDate end = budget.getEndDate();

        if (start == null || end == null) {
            return BigDecimal.ZERO;
        }

        Timestamp startTs = Timestamp.valueOf(LocalDateTime.of(start, java.time.LocalTime.MIN));
        Timestamp endTs = Timestamp.valueOf(LocalDateTime.of(end, java.time.LocalTime.MAX));

        List<Transaction> txs = transactionRepository.findByUserIdAndTransactionDateBetween(
                budget.getUserId(),
                startTs,
                endTs
        );

        return txs.stream()
                .filter(t -> t.getCategoryId().equals(budget.getCategoryId()))
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(t -> t.getAmount() != null ? t.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
