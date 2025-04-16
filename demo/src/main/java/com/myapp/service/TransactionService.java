package com.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.enums.TransactionStatus;
import com.myapp.model.enums.TransactionType;
import com.myapp.repository.TransactionRepository;

public class TransactionService {

    private final TransactionRepository transactionRepository;
    private int transactionIdCounter = 1;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction transfer(Account from, Account to, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionIdCounter++);
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        if (from.getBalance().compareTo(amount) >= 0) {
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
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
        return transactionRepository.findByAccountId(account.getId());
    }
}
