package com.myapp.controller;

import java.util.List;
import java.util.Scanner;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final Scanner scanner;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.scanner = new Scanner(System.in);
    }

    // Tüm işlemleri listele
    public void showAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        if (transactions == null || transactions.isEmpty()) { // Null kontrolü eklendi
            System.out.println("Henüz hiç işlem yapılmamış.");
        } else {
            for (Transaction tx : transactions) {
                System.out.println(tx);
            }
        }
    }

    // Belirli bir hesabın işlemlerini göster
    public void showTransactionsForAccount() {
        System.out.print("İşlemlerini görmek istediğiniz hesap ID'sini girin: ");
        if (!scanner.hasNextLong()) { // Kullanıcı girişinin geçerli bir sayı olup olmadığını kontrol et
            System.out.println("Geçersiz hesap ID'si girdiniz!");
            scanner.next(); // Hatalı girdiyi temizle
            return;
        }

        long accountId = scanner.nextLong();
        scanner.nextLine(); // Giriş tamponunu temizle

        Account account = accountService.findById(accountId).orElse(null);
        if (account == null) {
            System.out.println("Hesap bulunamadı!");
            return;
        }

        List<Transaction> txList = transactionService.getTransactionsForAccount(account);
        if (txList == null || txList.isEmpty()) { // Null kontrolü eklendi
            System.out.println("Bu hesaba ait işlem bulunamadı.");
        } else {
            for (Transaction tx : txList) {
                System.out.println(tx);
            }
        }
    }
}
