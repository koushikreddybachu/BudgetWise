package com.koushik.expansetracker.service.finance.implementations;
import com.koushik.expansetracker.entity.finance.Tag;
import com.koushik.expansetracker.entity.finance.Transaction;
import com.koushik.expansetracker.entity.finance.TransactionTag;
import com.koushik.expansetracker.repository.finance.TagRepository;
import com.koushik.expansetracker.repository.finance.TransactionRepository;
import com.koushik.expansetracker.repository.finance.TransactionTagRepository;
import com.koushik.expansetracker.security.OwnershipValidator;
import com.koushik.expansetracker.service.finance.interfaces.TransactionServiceInterface;
import com.koushik.expansetracker.util.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService implements TransactionServiceInterface {

    private final TransactionRepository transactionRepository;
    private final TagRepository tagRepository;
    private final TransactionTagRepository transactionTagRepository;
    private final OwnershipValidator ownershipValidator;

    public TransactionService(TransactionRepository transactionRepository,
                              TagRepository tagRepository,
                              TransactionTagRepository transactionTagRepository,
                              OwnershipValidator ownershipValidator) {
        this.transactionRepository = transactionRepository;
        this.tagRepository = tagRepository;
        this.transactionTagRepository = transactionTagRepository;
        this.ownershipValidator = ownershipValidator;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction, List<String> tagNames) {
        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(Timestamp.from(Instant.now()));
        }
        Transaction saved = transactionRepository.save(transaction);

        if (tagNames != null && !tagNames.isEmpty()) {

            // FIX: Remove duplicates to prevent unique constraint issues
            tagNames = tagNames.stream().distinct().toList();

            for (String name : tagNames) {

                Tag tag = tagRepository.findByTagName(name)
                        .orElseGet(() -> tagRepository.save(
                                Tag.builder().tagName(name).build()
                        ));

                transactionTagRepository.save(
                        TransactionTag.builder()
                                .transactionId(saved.getTransactionId())
                                .tagId(tag.getTagId())
                                .build()
                );
            }
        }


        return saved;
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long transactionId, Transaction updated, List<String> tagNames) {

        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, currentUserId);

        Transaction existing = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        existing.setAccountId(updated.getAccountId());
        existing.setCategoryId(updated.getCategoryId());
        existing.setAmount(updated.getAmount());
        existing.setType(updated.getType());
        existing.setDescription(updated.getDescription());
        existing.setTransactionDate(updated.getTransactionDate());

        transactionTagRepository.deleteByTransactionId(transactionId);

        // Save updated transaction values
        Transaction saved = transactionRepository.save(existing);

        // Handle tags
        if (tagNames != null && !tagNames.isEmpty()) {

            // Remove duplicates to avoid unique constraint error
            tagNames = tagNames.stream().distinct().toList();

            for (String name : tagNames) {

                Tag tag = tagRepository.findByTagName(name)
                        .orElseGet(() -> tagRepository.save(
                                Tag.builder().tagName(name).build()
                        ));

                transactionTagRepository.save(
                        TransactionTag.builder()
                                .transactionId(transactionId) // correct use
                                .tagId(tag.getTagId())
                                .build()
                );
            }
        }

        return saved;
    }

    @Override
    @Transactional
    public void deleteTransaction(Long transactionId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, currentUserId);
        transactionTagRepository.deleteByTransactionId(transactionId);
        transactionRepository.deleteById(transactionId);
    }


    @Override
    public Transaction getTransactionById(Long transactionId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        ownershipValidator.validateTransaction(transactionId, currentUserId);

        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getTransactionsForUser(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    @Override
    public List<Transaction> getTransactionsForUserInRange(Long userId, Timestamp start, Timestamp end) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, start, end);
    }
}
