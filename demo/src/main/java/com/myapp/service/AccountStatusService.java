package com.myapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.myapp.model.Account;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.repository.AccountRepository;

@Service
public class AccountStatusService implements IAccountStatusService {
    private static final Logger logger = LoggerFactory.getLogger(AccountStatusService.class);
    
    private final AccountRepository accountRepository;

    public AccountStatusService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void freezeAccount(long accountId) {
        logger.info("Freezing account with ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
            
        if (account.getOwnerName().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("Admin hesabı dondurulamaz!");
        }
        
        account.setFreezeStatus(FreezeStatus.FROZEN);
        accountRepository.save(account);
    }

    @Override
    public void unfreezeAccount(long accountId) {
        logger.info("Unfreezing account with ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
            
        account.setFreezeStatus(FreezeStatus.ACTIVE);
        accountRepository.save(account);
    }
}
