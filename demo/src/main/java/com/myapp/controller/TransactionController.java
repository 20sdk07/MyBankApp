package com.myapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

@RestController
@RequestMapping("/transactions") // Bu base path tüm transaction endpointleri için
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @GetMapping // GET /transactions
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}") // GET /transactions/account/{accountId}
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (!accountOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountOptional.get());
        if (transactions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transactions);
    }
}