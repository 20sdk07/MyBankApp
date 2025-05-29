package com.myapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.Users;

@Service
public class AdminService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final UserService userService;
    private final FreezeRequestService freezeRequestService;

    public AdminService(AccountService accountService,
                       TransactionService transactionService,
                       UserService userService,
                       FreezeRequestService freezeRequestService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.freezeRequestService = freezeRequestService;
    }    public Users getUserWithAccounts(UUID userId) {
        return userService.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Kullanıcı bulunamadı: " + userId));
    }

    public List<Account> getUserAccounts(UUID userId) {
        return accountService.getAccountsByUserId(userId);
    }

    public List<Account> getValidTransferAccounts(long currentAccountId) {
        List<Account> transferAccounts = accountService.getAllAccounts();
        List<Account> validTransferAccounts = new ArrayList<>();
        
        for (Account acc : transferAccounts) {
            if (!acc.isDeleted() && 
                acc.getId() != currentAccountId && 
                acc.getFreezeStatus() != null && 
                acc.getFreezeStatus().name().equals("ACTIVE")) {
                validTransferAccounts.add(acc);
            }
        }
        return validTransferAccounts;
    }

    @Transactional
    public void performAdminTransfer(long fromAccountId, long toAccountId, BigDecimal amount) {
        transactionService.transfer(fromAccountId, toAccountId, amount);
    }

    public Account getAccountDetails(long accountId) {
        return accountService.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı: " + accountId));
    }

    public List<Transaction> getAccountTransactions(Account account) {
        return transactionService.getTransactionsForAccount(account);
    }

    @Transactional
    public void freezeAccountByAdmin(long accountId) {
        accountService.freezeAccountByAdmin(accountId);
    }

    @Transactional
    public void unfreezeAccount(long accountId) {
        accountService.unfreezeAccount(accountId);
    }

    @Transactional
    public void deleteAccount(long accountId) {
        accountService.deleteAccount(accountId);
    }

    public boolean hasPendingFreezeRequest(long accountId) {
        return freezeRequestService.existsPendingRequestForAccount(accountId);
    }

    public List<Account> getFrozenAccounts() {
        return accountService.getFrozenAccounts();
    }
}
