package com.myapp.controller;

import java.math.BigDecimal;
import java.util.Optional;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public Account createAccount(String name, BigDecimal initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        return accountService.createAccount(name, initialBalance);
    }

    public Optional<Account> login(long accountId) {
        return accountService.findById(accountId);
    }

    public Transaction transfer(Account from, long toId, BigDecimal amount) {
        if (from == null) {
            throw new IllegalStateException("Sender account cannot be null.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        Account toAccount = accountService.findById(toId)
                                          .orElseThrow(() -> new IllegalArgumentException("Recipient account not found."));
        return transactionService.transfer(from, toAccount, amount);
    }

    public Account viewAccountInfo(long accountId) {
        return accountService.findById(accountId)
                              .orElseThrow(() -> new IllegalArgumentException("Account not found."));
    }
}
