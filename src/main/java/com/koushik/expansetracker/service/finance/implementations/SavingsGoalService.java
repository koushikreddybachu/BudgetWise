package com.koushik.expansetracker.service.finance.implementations;
import com.koushik.expansetracker.entity.finance.SavingsGoal;
import com.koushik.expansetracker.repository.finance.SavingsGoalRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.SavingsGoalServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SavingsGoalService implements SavingsGoalServiceInterface {

    private final SavingsGoalRepository savingsGoalRepository;
    private final OwnershipValidator ownershipValidator;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository,
                              OwnershipValidator ownershipValidator) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    public SavingsGoal createGoal(SavingsGoal goal) {
        if (goal.getCurrentAmount() == null) {
            goal.setCurrentAmount(BigDecimal.ZERO);
        }
        return savingsGoalRepository.save(goal);
    }

    @Override
    public List<SavingsGoal> getGoalsForUser(Long userId) {
        return savingsGoalRepository.findByUserId(userId);
    }

    @Override
    public SavingsGoal getGoalById(Long goalId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, currentUserId);

        return savingsGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Savings goal not found"));
    }

    @Override
    public SavingsGoal updateGoal(Long goalId, SavingsGoal updated) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, currentUserId);

        SavingsGoal existing = savingsGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Savings goal not found"));

        existing.setGoalName(updated.getGoalName());
        existing.setTargetAmount(updated.getTargetAmount());
        existing.setDeadline(updated.getDeadline());

        return savingsGoalRepository.save(existing);
    }

    @Override
    public SavingsGoal addToGoal(Long goalId, BigDecimal amount) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, currentUserId);

        SavingsGoal existing = savingsGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Savings goal not found"));

        if (existing.getCurrentAmount() == null) {
            existing.setCurrentAmount(BigDecimal.ZERO);
        }

        existing.setCurrentAmount(existing.getCurrentAmount().add(amount));

        return savingsGoalRepository.save(existing);
    }

    @Override
    public void deleteGoal(Long goalId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateSavingsGoal(goalId, currentUserId);
        savingsGoalRepository.deleteById(goalId);
    }
}
