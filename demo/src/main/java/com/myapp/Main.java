package com.myapp;

import java.math.BigDecimal;
import java.util.List;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.repository.AccountRepository;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class Main {
    public static void main(String[] args) {
        // Repository'leri oluştur
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Service'leri oluştur
        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService(transactionRepository);

        // Hesapları oluştur
        Account sadik = accountService.createAccount("Sadık", new BigDecimal("1000.00"));
        Account ali = accountService.createAccount("Ali", new BigDecimal("500.00"));

        System.out.println("Oluşturulan Hesaplar:");
        for (Account acc : accountService.getAllAccounts()) {
            System.out.println("- " + acc.getOwnerName() + " | Bakiye: " + acc.getBalance());
        }

        // Transfer işlemi yap
        Transaction tx = transactionService.transfer(sadik, ali, new BigDecimal("300.00"));
        System.out.println("\nTransfer Durumu: " + tx.getStatus());

        // Güncel bakiyeleri göster
        System.out.println("\nGüncel Hesap Bilgileri:");
        System.out.println(sadik.getOwnerName() + ": " + sadik.getBalance());
        System.out.println(ali.getOwnerName() + ": " + ali.getBalance());

        // İşlem geçmişi
        System.out.println("\nİşlem Geçmişi:");
        List<Transaction> transactions = transactionService.getAllTransactions();
        for (Transaction t : transactions) {
            System.out.println(t.getFromAccount().getOwnerName() + " --> " +
                               t.getToAccount().getOwnerName() + " | Tutar: " +
                               t.getAmount() + " | Durum: " + t.getStatus());
        }
    }
}
