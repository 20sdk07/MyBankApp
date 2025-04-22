package com.myapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.myapp.model.Account;
import com.myapp.repository.AccountRepository;

public class AccountService {
    private final AccountRepository accountRepository;
    private final List<Account> accounts = new ArrayList<>();
    private int nextAccountId = 1;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Yeni hesap oluşturur
    public Account createAccount(String owner, BigDecimal initialBalance) {
        Account account = new Account();
        account.setId(nextAccountId++);
        account.setOwnerName(owner);
        account.setBalance(initialBalance);
        accounts.add(account);
        return account;
    }

    // ID'ye göre hesap getirir
    public Optional<Account> findById(long id) {
    return accounts.stream()
                   .filter(account -> account.getId() == id)
                   .findFirst();
}

    // Tüm hesapları döner
    public List<Account> getAllAccounts() {
        return accounts;
    }
}
