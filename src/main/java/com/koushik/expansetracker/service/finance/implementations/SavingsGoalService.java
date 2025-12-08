package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.SavingsGoal;
import com.koushik.expansetracker.repository.finance.SavingsGoalRepository;
import com.koushik.expansetracker.service.finance.interfaces.SavingsGoalServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SavingsGoalService implements SavingsGoalServiceInterface {

    private final SavingsGoalRepository savingsGoalRepository;

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
        return savingsGoalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Savings goal not found with id: " + goalId));
    }

    @Override
    public SavingsGoal updateGoal(Long goalId, SavingsGoal updatedGoal) {
        SavingsGoal existing = getGoalById(goalId);
        existing.setGoalName(updatedGoal.getGoalName());
        existing.setTargetAmount(updatedGoal.getTargetAmount());
        existing.setCurrentAmount(updatedGoal.getCurrentAmount());
        existing.setDeadline(updatedGoal.getDeadline());
        return savingsGoalRepository.save(existing);
    }

    @Override
    public void deleteGoal(Long goalId) {
        savingsGoalRepository.deleteById(goalId);
    }

    @Override
    public SavingsGoal addToGoal(Long goalId, BigDecimal amount) {
        SavingsGoal existing = getGoalById(goalId);
        if (existing.getCurrentAmount() == null) {
            existing.setCurrentAmount(BigDecimal.ZERO);
        }
        existing.setCurrentAmount(existing.getCurrentAmount().add(amount));
        return savingsGoalRepository.save(existing);
    }
}
