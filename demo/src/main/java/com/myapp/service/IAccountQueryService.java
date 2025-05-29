package com.myapp.service;

import java.util.List;
import java.util.Optional;
import com.myapp.model.Account;

public interface IAccountQueryService {
    Optional<Account> findById(long id);
    List<Account> getAccountsByOwnerName(String ownerName);
    List<Account> getAllAccounts();
    List<Account> getFrozenAccounts();
}
