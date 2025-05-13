package com.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.myapp.model.Account;

@Repository
public class AccountRepository {

    private final List<Account> accounts = new ArrayList<>();

    public void save(Account account) {
        accounts.add(account);
    }

    public Optional<Account> findById(long id) {
        return accounts.stream()
                       .filter(account -> account.getId() == id)
                       .findFirst();
    }

    public List<Account> findAll() {
        return accounts;
    }
}
