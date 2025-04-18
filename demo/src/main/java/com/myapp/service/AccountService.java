package com.myapp.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.myapp.model.Account;

public class AccountService {
    private final List<Account> accounts = new ArrayList<>();
    private int nextAccountId = 1;

    
    //login 
    public Account login(String username, String password) {
        for (Account acc : accounts) {
            if (acc.getUsername().equals(username) && acc.getPassword().equals(password)) {
                return acc;
            }
        }
        return null; // giriş başarısız
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
    public Account findById(long id) {
        for (Account acc : accounts) {
            if (acc.getId() == id) {
                return acc;
            }
        }
        return null;
    }

    // Tüm hesapları döner
    public List<Account> getAllAccounts() {
        return accounts;
    }
}
