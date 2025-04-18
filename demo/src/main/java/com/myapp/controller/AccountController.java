package com.myapp.controller;

import java.math.BigDecimal;
import java.util.Scanner;

import com.myapp.model.Account;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class AccountController {
    AccountService accountService = new AccountService();
    TransactionRepository transactionRepository = new TransactionRepository();
    TransactionService transactionService = new TransactionService(transactionRepository);


    public void createAccount(Scanner scanner) {
        System.out.print("Hesap sahibinin adı: ");
        String name = scanner.nextLine();
        System.out.print("Başlangıç bakiyesi: ");
        BigDecimal balance = new BigDecimal(scanner.nextLine());

        Account account = accountService.createAccount(name, balance);
        System.out.println("Hesap oluşturuldu. Hesap ID: " + account.getId());
    }

    public void transferMoney(Scanner scanner) {
        System.out.print("Gönderen Hesap ID: ");
        long fromId = Long.parseLong(scanner.nextLine());
        System.out.print("Alıcı Hesap ID: ");
        long toId = Long.parseLong(scanner.nextLine());
        System.out.print("Miktar: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        Account from = accountService.findById(fromId);
        Account to = accountService.findById(toId);

        if (from != null && to != null) {
            transactionService.transfer(from, to, amount);
        } else {
            System.out.println("Hesap bulunamadı.");
        }
    }

    public void printAccountInfo(Scanner scanner) {
        System.out.print("Hesap ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Account account = accountService.findById(id);

        if (account != null) {
            System.out.println("Hesap Sahibi: " + account.getOwnerName());
            System.out.println("Bakiye: " + account.getBalance());
        } else {
            System.out.println("Hesap bulunamadı.");
        }
    }
    //login method
    public void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
    
        System.out.print("Password: ");
        String password = scanner.nextLine();
    
        Account account = accountService.login(username, password);
        if (account != null) {
            System.out.println("Login successful! Welcome, " + account.getOwnerName());
        } else {
            System.out.println("Login failed. Invalid credentials.");
        }
    }
    
}
