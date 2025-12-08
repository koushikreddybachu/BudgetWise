package com.koushik.expansetracker.controller.finance;

import com.koushik.expansetracker.dto.BudgetRequest;
import com.koushik.expansetracker.dto.BudgetResponse;
import com.koushik.expansetracker.entity.finance.Budget;
import com.koushik.expansetracker.mapper.FinanceMapper;
import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.service.finance.interfaces.BudgetServiceInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetServiceInterface budgetService;
    private final FinanceMapper mapper;

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody BudgetRequest request
    ) {
        Long userId = currentUser.getUser().getUserId();

        Budget saved = budgetService.createBudget(
                mapper.toBudgetEntity(request, userId)
        );

        BigDecimal spent = budgetService.calculateSpentForBudget(saved.getBudgetId());

        return ResponseEntity.ok(mapper.toBudgetResponse(saved, spent));
    }

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getBudgets(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        Long userId = currentUser.getUser().getUserId();

        var list = budgetService.getBudgetsForUser(userId)
                .stream()
                .map(b -> mapper.toBudgetResponse(
                        b,
                        budgetService.calculateSpentForBudget(b.getBudgetId())
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> getBudgetById(@PathVariable Long budgetId) {
        Budget b = budgetService.getBudgetById(budgetId);
        BigDecimal spent = budgetService.calculateSpentForBudget(budgetId);
        return ResponseEntity.ok(mapper.toBudgetResponse(b, spent));
    }

    @PutMapping("/{budgetId}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long budgetId,
            @Valid @RequestBody BudgetRequest request
    ) {
        Budget updated = budgetService.updateBudget(budgetId, mapper.toBudgetEntity(request, null));
        BigDecimal spent = budgetService.calculateSpentForBudget(updated.getBudgetId());
        return ResponseEntity.ok(mapper.toBudgetResponse(updated, spent));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.ok("Budget deleted successfully.");
    }
}
