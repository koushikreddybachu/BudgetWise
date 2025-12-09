package com.koushik.expansetracker.scheduler;

import com.koushik.expansetracker.service.finance.implementations.RecurringTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecurringTransactionScheduler {

    private final RecurringTransactionService recurringTransactionService;

    public RecurringTransactionScheduler(RecurringTransactionService recurringTransactionService) {
        this.recurringTransactionService = recurringTransactionService;
    }

    /**
     * Runs every hour
     * cron: second minute hour day month weekday
     * 0 0 * * * * = at minute 0, hour 0, every day
     */
    @Scheduled(cron = "0 0 * * * *") // every hour
    public void processRecurringTransactions() {

        log.info("Running RecurringTransactionScheduler...");

        try {
            recurringTransactionService.processDueRecurringTransactions();
            log.info("Recurring transactions processed successfully.");
        } catch (Exception e) {
            log.error("Error processing recurring transactions: {}", e.getMessage());
        }
    }
}
