package com.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.enums.TransactionStatus;
import com.myapp.model.enums.TransactionType;

public class TransactionService {

    private final List<Transaction> transactions = new ArrayList<>();
    private int transactionIdCounter = 1;

    // Yeni işlem (transfer) oluşturur
    public Transaction transfer(Account from, Account to, BigDecimal amount) {
        // Null kontrolleri
        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Transaction transaction = new Transaction();
        transaction.setId(transactionIdCounter++);
        transaction.setFromAccount(from);
        transaction.setToAccount(to);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(TransactionType.TRANSFER);

        // Hesaplardan bakiye düşme

        if (from.getBalance().compareTo(amount) >= 0) {
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));

            transaction.setStatus(TransactionStatus.SUCCESS);
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
        }

        transactions.add(transaction);
        return transaction;
    }

    // Tüm işlemleri getir
    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    // Belirli bir hesaba ait işlemleri getir
    public List<Transaction> getTransactionsForAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.getFromAccount().equals(account) || tx.getToAccount().equals(account)) {
                result.add(tx);
            }
        }
        return result;
    }
}

