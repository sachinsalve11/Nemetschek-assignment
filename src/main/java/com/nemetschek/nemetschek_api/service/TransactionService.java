package com.nemetschek.nemetschek_api.service;

import com.nemetschek.nemetschek_api.model.Account;
import com.nemetschek.nemetschek_api.model.Transaction;
import com.nemetschek.nemetschek_api.model.TransactionType;
import com.nemetschek.nemetschek_api.repo.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service

public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;
/*
    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }*/

    @Transactional
    public Transaction makeTransaction(Long accountId, BigDecimal amount, TransactionType type) {

        Account account = accountService.getAccount(accountId);

        // Fraud Detection Logic
        if (type == TransactionType.OUT && exceedsDailyLimit(accountId, amount)) {
            throw new RuntimeException("Fraud detected: Daily withdrawal limit exceeded");
        }

        // Balance Check
        if (type == TransactionType.OUT && account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // Update Balance
        BigDecimal newBalance = type == TransactionType.IN
                ? account.getBalance().add(amount)
                : account.getBalance().subtract(amount);
        account.setBalance(newBalance);
        accountService.updateAccount(accountId, account);

        // Save Transaction

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(type);
        return transactionRepository.save(transaction);
    }

    private boolean exceedsDailyLimit(Long accountId, BigDecimal amount) {

        // Logic to sum all 'OUT' transactions for the day and check against $10,000 limit
        List<Transaction> todayTransactions = transactionRepository.findByAccountIdAndTimestampBetween(
                accountId, LocalDateTime.now().withHour(0).withMinute(0), LocalDateTime.now());
        BigDecimal dailyTotal = todayTransactions.stream()
                .filter(t -> t.getType() == TransactionType.OUT)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return dailyTotal.add(amount).compareTo(BigDecimal.valueOf(10000)) > 0;
    }
}
