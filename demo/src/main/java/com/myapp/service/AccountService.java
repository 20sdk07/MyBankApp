package com.myapp.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.model.Account;
import com.myapp.model.Users;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.repository.AccountRepository;
import com.myapp.repository.UserRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository; // Eğer yoksa ekle

    // Yeni hesap oluşturur
    public Account createAccount(String owner, BigDecimal initialBalance) {
        Account account = new Account();
        account.setOwnerName(owner);
        account.setBalance(initialBalance);
        // freezeStatus ve diğer alanlar zaten default
        return accountRepository.save(account); // JPA ile kaydediliyor
    }

    // ID'ye göre hesap getirir
    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    // Tüm hesapları getirir
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }    // Hesap dondurma işlemi
    public void freezeAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı: " + accountId));
            
        // Admin hesabı kontrolü
        if (account.getOwnerName().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("Admin hesabı dondurulamaz!");
        }
        
        // Zaten dondurulmuş mu kontrolü
        if (account.getFreezeStatus() == FreezeStatus.FROZEN) {
            throw new IllegalArgumentException("Hesap zaten dondurulmuş durumda!");
        }
        
        // Silinmiş hesap kontrolü
        if (account.isDeleted()) {
            throw new IllegalArgumentException("Silinmiş bir hesap dondurulamaz!");
        }
        
        account.setFreezeStatus(FreezeStatus.FROZEN);
        accountRepository.save(account);
    }    // Hesap dondurmayı kaldırma işlemi
    public void unfreezeAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı: " + accountId));
        
        // Zaten aktif mi kontrolü
        if (account.getFreezeStatus() == FreezeStatus.ACTIVE) {
            throw new IllegalArgumentException("Hesap zaten aktif durumda!");
        }
        
        // Silinmiş hesap kontrolü
        if (account.isDeleted()) {
            throw new IllegalArgumentException("Silinmiş bir hesabın dondurma durumu değiştirilemez!");
        }
        
        account.setFreezeStatus(FreezeStatus.ACTIVE);
        accountRepository.save(account);
    }
    
    // Hesap silme işlemi
    public void deleteAccount(long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with ID: " + accountId));
        if ("admin".equalsIgnoreCase(account.getOwnerName())) {
            throw new IllegalArgumentException("Admin hesabı silinemez!");
        }
        account.setDeleted(true);
        accountRepository.save(account);
    }
    
    public List<Account> getAccountsByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        return accountRepository.findByOwnerName(user.getUsername())
            .stream()
            .filter(acc -> !acc.isDeleted())
            .toList();
    }

    public List<Account> getAccountsByOwnerName(String ownerName) {
        return accountRepository.findByOwnerName(ownerName)
            .stream().filter(acc -> !acc.isDeleted()).toList();
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    // Donuk hesapları getir
    public List<Account> getFrozenAccounts() {
        return accountRepository.findAll().stream()
            .filter(acc -> acc.getFreezeStatus() == FreezeStatus.FROZEN && !acc.isDeleted())
            .toList();
    }

    // Admin tarafından hesap dondurma işlemi (istek olmadan)
    public void freezeAccountByAdmin(long accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Hesap bulunamadı: " + accountId));
            
        if (account.getOwnerName().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("Admin hesabı dondurulamaz!");
        }
        
        if (account.getFreezeStatus() == FreezeStatus.FROZEN) {
            throw new IllegalArgumentException("Hesap zaten dondurulmuş durumda!");
        }
        
        account.setFreezeStatus(FreezeStatus.FROZEN);
        accountRepository.save(account);
    }
}
