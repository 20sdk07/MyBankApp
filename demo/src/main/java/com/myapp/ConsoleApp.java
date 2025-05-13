/*
package com.myapp;

import java.math.BigDecimal;
import java.util.Scanner;

import com.myapp.controller.AccountController;
import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.enums.TransactionStatus;

public class ConsoleApp {

    private final AccountController controller;
    private final Scanner scanner = new Scanner(System.in);
    private Account loggedInAccount = null;

    public ConsoleApp(AccountController controller) {
        this.controller = controller;
    }

    public void start() {
        while (true) {
            System.out.println("\n--- Welcome to My Bank App ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = readInt();

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
        String name = scanner.nextLine();

        System.out.print("Enter initial balance: ");
        BigDecimal balance = readBigDecimal();

        Account account = controller.createAccount(name, balance);
        System.out.println("Account created successfully. ID: " + account.getId());
    }

    private void login() {
        System.out.print("Enter your account ID: ");
        long id = readLong();

        controller.login(id).ifPresentOrElse(account -> {
            loggedInAccount = account;
            System.out.println("Login successful. Welcome, " + loggedInAccount.getOwnerName());
            showAccountMenu();
        }, () -> System.out.println("Account not found."));
    }

    private void showAccountMenu() {
        while (loggedInAccount != null) {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. View Account Info");
            System.out.println("2. Transfer Money");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");

            int choice = readInt();

            switch (choice) {
                case 1 -> viewAccountInfo();
                case 2 -> transferMoney();
                case 3 -> logout();
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
        long toId = readLong();

        System.out.print("Enter amount to transfer: ");
        BigDecimal amount = readBigDecimal();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        try {
            Transaction tx = controller.transfer(loggedInAccount, toId, amount);
            if (tx.getStatus() == TransactionStatus.SUCCESS) {
                System.out.println("Transfer successful.");
            } else {
                System.out.println("Transfer failed: insufficient funds.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void logout() {
        System.out.println("Logged out.");
        loggedInAccount = null;
    }

    // --- Yardımcı güvenli okuma metodları ---
    private int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter an integer: ");
            }
        }
    }

    private long readLong() {
        while (true) {
            try {
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a long number: ");
            }
        }
    }

    private BigDecimal readBigDecimal() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a decimal number: ");
            }
        }
    }
}
 */