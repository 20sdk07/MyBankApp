package com.myapp.controller;

import com.myapp.model.Account;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

public class AccountController {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private Account loggedInAccount = null;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            System.out.println("\n--- Welcome to My Bank App ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> login();
                case 3 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createAccount() {
        System.out.print("Enter your name: ");
        String name = scanner.next();

        System.out.print("Enter initial balance: ");
        BigDecimal balance = scanner.nextBigDecimal();

        Account account = accountService.createAccount(name, balance);
        System.out.println("Account created successfully. ID: " + account.getId());
    }

    private void login() {
        System.out.print("Enter your account ID: ");
        long id = scanner.nextLong();

        Optional<Account> optionalAccount = accountService.findById(id);
        if (optionalAccount.isPresent()) {
            loggedInAccount = optionalAccount.get();
            System.out.println("Login successful. Welcome, " + loggedInAccount.getOwnerName());
            showAccountMenu();
        } else {
            System.out.println("Account not found.");
        }
    }

    private void showAccountMenu() {
        while (true) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. View Account Info");
            System.out.println("2. Transfer Money");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewAccountInfo();
                case 2 -> transferMoney();
                case 3 -> {
                    System.out.println("Logged out.");
                    loggedInAccount = null;
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAccountInfo() {
        System.out.println("Account ID: " + loggedInAccount.getId());
        System.out.println("Owner: " + loggedInAccount.getOwnerName());
        System.out.println("Balance: " + loggedInAccount.getBalance());
    }

    private void transferMoney() {
        System.out.print("Enter recipient account ID: ");
        long toId = scanner.nextLong();

        System.out.print("Enter amount to transfer: ");
        BigDecimal amount = scanner.nextBigDecimal();

        Optional<Account> toAccountOptional = accountService.findById(toId);
        if (toAccountOptional.isPresent()) {
            Account toAccount = toAccountOptional.get();
            transactionService.transfer(loggedInAccount, toAccount, amount);
            System.out.println("Transfer completed.");
        } else {
            System.out.println("Recipient account not found.");
        }
    }
}
