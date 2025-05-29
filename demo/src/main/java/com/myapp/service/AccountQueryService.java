package com.myapp.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.myapp.model.Account;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.repository.AccountRepository;

@Service
public class AccountQueryService implements IAccountQueryService {
    private static final Logger logger = LoggerFactory.getLogger(AccountQueryService.class);
    
    private final AccountRepository accountRepository;

    public AccountQueryService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<Account> findById(long id) {
        logger.debug("Finding account by ID: {}", id);
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAccountsByOwnerName(String ownerName) {
        logger.debug("Finding accounts for owner: {}", ownerName);
        return accountRepository.findByOwnerName(ownerName)
            .stream()
            .filter(acc -> !acc.isDeleted())
            .toList();
    }

    @Override
    public List<Account> getAllAccounts() {
        logger.debug("Retrieving all accounts");
        return accountRepository.findAll()
            .stream()
            .filter(acc -> !acc.isDeleted())
            .toList();
    }

    @Override
    public List<Account> getFrozenAccounts() {
        logger.debug("Retrieving frozen accounts");
        return accountRepository.findAll()
            .stream()
            .filter(acc -> acc.getFreezeStatus() == FreezeStatus.FROZEN && !acc.isDeleted())
            .toList();
    }
}
