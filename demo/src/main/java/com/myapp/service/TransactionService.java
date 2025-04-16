package com.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        // Null kontrolleri
        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Transaction transaction = new Transaction();
        transaction.setId (transactionIdCounter);
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        // Hesaplardan bakiye düşme kontrolü

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
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactionRepository.findAll()) { // Tüm işlemleri al
            if (tx.getFromAccount().equals(account) || tx.getToAccount().equals(account)) {
                result.add(tx); // Hesaba ait işlemleri ekle
            }
        }
        return result;
    }
}

