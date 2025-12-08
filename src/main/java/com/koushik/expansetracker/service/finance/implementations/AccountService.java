package com.koushik.expansetracker.service.finance.implementations;

import com.koushik.expansetracker.entity.finance.Account;
import com.koushik.expansetracker.repository.finance.AccountRepository;
import com.koushik.expansetracker.service.finance.interfaces.AccountServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountServiceInterface {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    @Override
    public List<Account> getAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }

    @Override
    public Account updateAccount(Long accountId, Account updatedAccount) {
        Account existing = getAccountById(accountId);
        existing.setAccountName(updatedAccount.getAccountName());
        existing.setAccountType(updatedAccount.getAccountType());
        existing.setCurrency(updatedAccount.getCurrency());
        // balance normally should be managed by transactions; update only if you allow manual override
        if (updatedAccount.getBalance() != null) {
            existing.setBalance(updatedAccount.getBalance());
        }
        return accountRepository.save(existing);
    }

    @Override
    public void deleteAccount(Long accountId) {
        // You *can* add a check here to avoid deleting account with transactions
        accountRepository.deleteById(accountId);
    }
}
