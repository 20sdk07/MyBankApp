package com.myapp.controller;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/accounts") // Bu base path tüm account endpointleri için
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping // POST /accounts
public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
    Account account = accountService.createAccount(request.getOwnerName(), request.getInitialBalance());
    return new ResponseEntity<>(account, HttpStatus.CREATED);
}

    @GetMapping("/{accountId}") // GET /accounts/{accountId}
    public ResponseEntity<Account> getAccount(@PathVariable long accountId) {
        Optional<Account> account = accountService.findById(accountId);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

   @PostMapping("/transfer") // POST /accounts/transfer
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request) {
    Optional<Account> fromAccountOptional = accountService.findById(request.getFromAccountId());
    if (!fromAccountOptional.isPresent()) {
        return ResponseEntity.badRequest().body("Gönderen hesap bulunamadı.");
    }
    Account fromAccount = fromAccountOptional.get();

    Optional<Account> toAccountOptional = accountService.findById(request.getToAccountId());
    if (!toAccountOptional.isPresent()) {
        return ResponseEntity.badRequest().body("Alıcı hesap bulunamadı.");
    }
    Account toAccount = toAccountOptional.get();

    if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
        return ResponseEntity.badRequest().body("Miktar sıfırdan büyük olmalı.");
    }

    try {
        Transaction transaction = transactionService.transfer(fromAccount, toAccount, request.getAmount());
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    

    @GetMapping("/{accountId}/info") // GET /accounts/{accountId}/info
    public ResponseEntity<Account> viewAccountInfo(@PathVariable long accountId) {
        Optional<Account> account = accountService.findById(accountId);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Hesap dondurma endpoint'i
    @PutMapping("/{accountId}/freeze") // PUT /accounts/{accountId}/freeze
    public ResponseEntity<Void> freezeAccount(@PathVariable long accountId) {
        try {
            accountService.freezeAccount(accountId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Hesap dondurmayı kaldırma endpoint'i
    @PutMapping("/{accountId}/unfreeze") // PUT /accounts/{accountId}/unfreeze
    public ResponseEntity<Void> unfreezeAccount(@PathVariable long accountId) {
        try {
            accountService.unfreezeAccount(accountId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Hesap silme (mantıksal silme) endpoint'i
    @DeleteMapping("/{accountId}") // DELETE /accounts/{accountId}
    public ResponseEntity<Void> deleteAccount(@PathVariable long accountId) {
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

// İstek gövdelerini (Request Body) karşılamak için basit POJO'lar (Plain Old Java Object) oluşturalım
class CreateAccountRequest {
    private String ownerName;
    private BigDecimal initialBalance;

    // Getter ve Setter'lar
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }
}

class TransferRequest {
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;

    // Getter ve Setter'lar
    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}