package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.*;
import com.koushik.expansetracker.entity.finance.enums.TransactionType;
import com.koushik.expansetracker.repository.finance.*;
import com.koushik.expansetracker.service.finance.interfaces.TransactionServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService implements TransactionServiceInterface {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final TransactionTagRepository transactionTagRepository;

    @Override
    public Transaction createTransaction(Transaction transaction, List<String> tagNames) {
        validateAccountAndCategory(transaction);

        // set createdAt if null
        if (transaction.getCreatedAt() == null) {
            transaction.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(Timestamp.from(Instant.now()));
        }

        // update account balance
        applyBalanceEffect(transaction, null);

        Transaction saved = transactionRepository.save(transaction);

        // handle tags
        if (tagNames != null && !tagNames.isEmpty()) {
            saveTagsForTransaction(saved.getTransactionId(), tagNames);
        }

        return saved;
    }

    @Override
    public Transaction updateTransaction(Long transactionId, Transaction updatedTransaction, List<String> tagNames) {
        Transaction existing = getTransactionById(transactionId);

        // revert old balance effect
        revertBalanceEffect(existing);

        // update fields
        existing.setAmount(updatedTransaction.getAmount());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setTransactionDate(
                updatedTransaction.getTransactionDate() != null
                        ? updatedTransaction.getTransactionDate()
                        : existing.getTransactionDate()
        );
        existing.setType(updatedTransaction.getType());
        existing.setAccountId(updatedTransaction.getAccountId());
        existing.setCategoryId(updatedTransaction.getCategoryId());
        existing.setUserId(updatedTransaction.getUserId());

        validateAccountAndCategory(existing);

        // apply new balance effect
        applyBalanceEffect(existing, null);

        Transaction saved = transactionRepository.save(existing);

        // update tags (remove old + add new)
        transactionTagRepository.findByTransactionId(saved.getTransactionId())
                .forEach(tt -> transactionTagRepository.deleteById(tt.getId()));

        if (tagNames != null && !tagNames.isEmpty()) {
            saveTagsForTransaction(saved.getTransactionId(), tagNames);
        }

        return saved;
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        Transaction existing = getTransactionById(transactionId);

        // revert account balance
        revertBalanceEffect(existing);

        // delete tags mapping
        transactionTagRepository.findByTransactionId(existing.getTransactionId())
                .forEach(tt -> transactionTagRepository.deleteById(tt.getId()));

        transactionRepository.deleteById(transactionId);
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + transactionId));
    }

    @Override
    public List<Transaction> getTransactionsForUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactionsForUserInRange(Long userId, Timestamp start, Timestamp end) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }

    // ---------- Helper Methods ----------

    private void validateAccountAndCategory(Transaction tx) {
        Account account = accountRepository.findById(tx.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found: " + tx.getAccountId()));

        if (!account.getUserId().equals(tx.getUserId())) {
            throw new RuntimeException("Account does not belong to user");
        }

        Category category = categoryRepository.findById(tx.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + tx.getCategoryId()));

        if (category.getUserId() != null && !category.getUserId().equals(tx.getUserId())) {
            throw new RuntimeException("Category does not belong to user");
        }
    }

    private void applyBalanceEffect(Transaction tx, Account preLoadedAccount) {
        Account account = preLoadedAccount != null
                ? preLoadedAccount
                : accountRepository.findById(tx.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found while applying balance"));

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        BigDecimal amount = tx.getAmount() != null ? tx.getAmount() : BigDecimal.ZERO;
        BigDecimal delta = BigDecimal.ZERO;

        if (tx.getType() == TransactionType.EXPENSE) {
            delta = amount.negate();
        } else if (tx.getType() == TransactionType.INCOME) {
            delta = amount;
        } else if (tx.getType() == TransactionType.TRANSFER) {
            // For now we do nothing (need source & target accounts to support fully)
            delta = BigDecimal.ZERO;
        }

        account.setBalance(account.getBalance().add(delta));
        accountRepository.save(account);
    }

    private void revertBalanceEffect(Transaction tx) {
        Account account = accountRepository.findById(tx.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found while reverting balance"));

        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }

        BigDecimal amount = tx.getAmount() != null ? tx.getAmount() : BigDecimal.ZERO;
        BigDecimal delta = BigDecimal.ZERO;

        if (tx.getType() == TransactionType.EXPENSE) {
            delta = amount; // reverse of -amount
        } else if (tx.getType() == TransactionType.INCOME) {
            delta = amount.negate();
        } else if (tx.getType() == TransactionType.TRANSFER) {
            delta = BigDecimal.ZERO;
        }

        account.setBalance(account.getBalance().add(delta));
        accountRepository.save(account);
    }

    private void saveTagsForTransaction(Long transactionId, List<String> tagNames) {
        for (String tagName : tagNames) {
            if (tagName == null || tagName.isBlank()) continue;

            Tag tag = tagRepository.findByTagName(tagName.trim())
                    .orElseGet(() -> {
                        Tag t = Tag.builder().tagName(tagName.trim()).build();
                        return tagRepository.save(t);
                    });

            TransactionTag tt = TransactionTag.builder()
                    .transactionId(transactionId)
                    .tagId(tag.getTagId())
                    .build();

            transactionTagRepository.save(tt);
        }
    }
}
