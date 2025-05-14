package com.myapp.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.model.Account;
import com.myapp.model.FreezeRequest;
import com.myapp.model.Transaction;
import com.myapp.service.AccountService;
import com.myapp.service.FreezeRequestService;
import com.myapp.service.TransactionService;

@RestController
@RequestMapping("/accounts") // Bu base path tüm account endpointleri için
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final FreezeRequestService freezeRequestService;

    @Autowired
    public AccountController(AccountService accountService, TransactionService transactionService, FreezeRequestService freezeRequestService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.freezeRequestService = freezeRequestService;
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

     @PostMapping("/{accountId}/freeze-request")
    public ResponseEntity<?> requestFreezeAccount(@PathVariable Long accountId) {
        try {
            FreezeRequest freezeRequest = freezeRequestService.createFreezeRequest(accountId);
            return new ResponseEntity<>("Hesap dondurma isteğiniz alınmıştır. Yönetici onayını beklemektedir.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
