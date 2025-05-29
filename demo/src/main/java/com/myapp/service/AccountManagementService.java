package com.myapp.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.myapp.model.Account;
import com.myapp.repository.AccountRepository;

@Service
public class AccountManagementService implements IAccountManagementService {
    private static final Logger logger = LoggerFactory.getLogger(AccountManagementService.class);
    
    private final AccountRepository accountRepository;

    public AccountManagementService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }    @Override
    @org.springframework.transaction.annotation.Transactional
    public Account createAccount(String owner, BigDecimal initialBalance) {
        logger.info("Creating new account for owner: {}", owner);
        
        if (owner == null || owner.trim().isEmpty()) {
            logger.error("Account creation failed: owner name is null or empty");
            throw new IllegalArgumentException("Hesap sahibi adı gereklidir");
        }
        
        if (initialBalance == null || initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            logger.error("Account creation failed: invalid initial balance: {}", initialBalance);
            throw new IllegalArgumentException("Geçersiz başlangıç bakiyesi");
        }

        Account account = new Account();
        account.setOwnerName(owner);
        account.setBalance(initialBalance);
        account.setFreezeStatus(com.myapp.model.enums.FreezeStatus.ACTIVE);
        
        try {
            account = accountRepository.save(account);
            logger.info("Successfully created account with ID: {} for owner: {}", account.getId(), owner);
            return account;
        } catch (Exception e) {
            logger.error("Failed to create account for owner: {}", owner, e);
            throw new RuntimeException("Hesap oluşturulurken bir hata oluştu", e);
        }
    }

    @Override
    public void deleteAccount(long accountId) {
        logger.info("Deleting account with ID: {}", accountId);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        
        if ("admin".equalsIgnoreCase(account.getOwnerName())) {
            throw new IllegalArgumentException("Admin hesabı silinemez!");
        }
        
        account.setDeleted(true);
        accountRepository.save(account);
    }
}
