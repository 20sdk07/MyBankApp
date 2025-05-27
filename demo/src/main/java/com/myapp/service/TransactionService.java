package com.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.enums.TransactionStatus;
import com.myapp.model.enums.TransactionType;
import com.myapp.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private int nextTransactionId = 1; // Transaction ID sayacı

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        Account fromAccount = accountService.findById(fromAccountId)
            .orElseThrow(() -> new IllegalArgumentException("From account not found"));
        Account toAccount = accountService.findById(toAccountId)
            .orElseThrow(() -> new IllegalArgumentException("To account not found"));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }

        Transaction transaction = new Transaction();
        transaction.setId(nextTransactionId++);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        if (fromAccount.getBalance().compareTo(amount) >= 0) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
            transaction.setStatus(TransactionStatus.SUCCESS);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }

        transactionRepository.save(transaction);
        return transaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsForAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        return transactionRepository.findByAccountId(account.getId());
    }
}
