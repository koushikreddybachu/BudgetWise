package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.SavingsGoalRequest;
import com.koushik.expansetracker.dto.SavingsGoalResponse;
import com.koushik.expansetracker.entity.finance.SavingsGoal;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.SavingsGoalServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class SavingsGoalController {

    private final SavingsGoalServiceInterface savingsGoalService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<SavingsGoalResponse> createGoal(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody SavingsGoalRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();

        SavingsGoal saved = savingsGoalService.createGoal(
                mapper.toSavingsGoalEntity(request, userId)
        );

        return ResponseEntity.ok(mapper.toSavingsGoalResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<SavingsGoalResponse>> getGoals(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = savingsGoalService.getGoalsForUser(userId)
                .stream()
                .map(mapper::toSavingsGoalResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{goalId}")
    public ResponseEntity<SavingsGoalResponse> getGoalById(@PathVariable Long goalId) {
        SavingsGoal g = savingsGoalService.getGoalById(goalId);
        return ResponseEntity.ok(mapper.toSavingsGoalResponse(g));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<SavingsGoalResponse> updateGoal(
            @PathVariable Long goalId,
            @Valid @RequestBody SavingsGoalRequest request
    ) {
        SavingsGoal updated = savingsGoalService.updateGoal(goalId, mapper.toSavingsGoalEntity(request, null));
        return ResponseEntity.ok(mapper.toSavingsGoalResponse(updated));
    }

    @PatchMapping("/{goalId}/add")
    public ResponseEntity<SavingsGoalResponse> addToGoal(
            @PathVariable Long goalId,
            @RequestParam("amount") BigDecimal amount
    ) {
        SavingsGoal updated = savingsGoalService.addToGoal(goalId, amount);
        return ResponseEntity.ok(mapper.toSavingsGoalResponse(updated));
    }

    @DeleteMapping("/{goalId}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long goalId) {
        savingsGoalService.deleteGoal(goalId);
        return ResponseEntity.ok("Savings goal deleted successfully.");
    }
}
