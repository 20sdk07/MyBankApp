package com.myapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.exception.AccountFrozenException;
import com.myapp.exception.InsufficientBalanceException;
import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.model.enums.TransactionStatus;
import com.myapp.model.enums.TransactionType;
import com.myapp.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer tutarı sıfırdan büyük olmalıdır");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Aynı hesaba transfer yapamazsınız");
        }

        Account fromAccount = accountService.findById(fromAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Gönderen hesap bulunamadı: " + fromAccountId));
        Account toAccount = accountService.findById(toAccountId)
            .orElseThrow(() -> new IllegalArgumentException("Alıcı hesap bulunamadı: " + toAccountId));

        // Hesapların dondurulmuş olup olmadığını kontrol et
        if (fromAccount.getFreezeStatus() == FreezeStatus.FROZEN) {
            throw new AccountFrozenException("Gönderen hesap dondurulmuş durumda", fromAccountId);
        }
        if (toAccount.getFreezeStatus() == FreezeStatus.FROZEN) {
            throw new AccountFrozenException("Alıcı hesap dondurulmuş durumda", toAccountId);
        }

        // Bakiye kontrolü
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                "Yetersiz bakiye",
                fromAccountId,
                amount,
                fromAccount.getBalance()
            );
        }

        // Transfer işlemi
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setDescription(String.format("Transfer from account %d to %d", fromAccountId, toAccountId));
        transaction.setStatus(TransactionStatus.SUCCESS);

        // Bakiye güncellemeleri
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        // Değişiklikleri kaydet
        accountService.save(fromAccount);
        accountService.save(toAccount);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }    public List<Transaction> getTransactionsForAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        return transactionRepository.findByFromAccount_IdOrToAccount_Id(account.getId(), account.getId());
    }
}
