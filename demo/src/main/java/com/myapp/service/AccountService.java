package com.myapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.myapp.model.Account;
import com.myapp.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;
    private int accountIdCounter = 1;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(String ownerName, BigDecimal initialBalance) {
        Account account = new Account();
        account.setId(accountIdCounter++);
        account.setOwnerName(ownerName);
        account.setBalance(initialBalance);
        accountRepository.save(account);
        return account;
    }

    public Optional<Account> getAccountById(int id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
