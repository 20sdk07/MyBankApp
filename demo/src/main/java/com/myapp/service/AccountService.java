package com.myapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.myapp.model.Account;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.repository.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;
    private int nextAccountId = 1; // ID sayacı

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Yeni hesap oluşturur
    public Account createAccount(String owner, BigDecimal initialBalance) {
        Account account = new Account();
        account.setId(nextAccountId++);
        account.setOwnerName(owner);
        account.setBalance(initialBalance);

        accountRepository.save(account); // Kayıt işlemi sadece repository üzerinden
        return account;
    }

    // ID'ye göre hesap getirir
    public Optional<Account> findById(long id) {
        return accountRepository.findById((int) id); // Repository'de int ID kullanıyoruz
    }

    // Tüm hesapları getirir
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Hesap dondurma işlemi
    public void freezeAccount(long accountId) {
    Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
    account.setFreezeStatus(FreezeStatus.FROZEN);
    accountRepository.save(account);
    }

    // Hesap dondurmayı kaldırma işlemi
    public void unfreezeAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        account.setFreezeStatus(FreezeStatus.ACTIVE);
        accountRepository.save(account);
    }
    
    // Hesap silme işlemi
    public void deleteAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        account.setDeleted(true);
        accountRepository.save(account);
    }
    

}
