package com.myapp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            AccountService accountService = new AccountService();
            TransactionRepository transactionRepository = new TransactionRepository();
            TransactionService transactionService = new TransactionService(transactionRepository);

            Account loggedInUser = null;

            while (true) {
                if (loggedInUser == null) {
                    System.out.println("\n=== Welcome to My Bank App ===");
                    System.out.println("1. Create Account");
                    System.out.println("2. Login");
                    System.out.println("0. Exit");
                    System.out.print("Choose an option: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (choice) {
                        case 1 -> {
                            System.out.print("Enter your name: ");
                            String name = scanner.nextLine();
                            System.out.print("Enter initial balance: ");
                            BigDecimal balance = scanner.nextBigDecimal();
                            scanner.nextLine(); // consume newline
                            loggedInUser = accountService.createAccount(name, balance);
                            System.out.println("Account created. Logged in as " + loggedInUser.getOwnerName());
                        }
                        case 2 -> {
                            System.out.print("Enter your account ID: ");
                            long id = scanner.nextLong();
                            scanner.nextLine(); // consume newline
                            Account found = accountService.findById(id);
                            if (found != null) {
                                loggedInUser = found;
                                System.out.println("Login successful. Welcome " + loggedInUser.getOwnerName());
                            } else {
                                System.out.println("Account not found.");
                            }
                        }
                        case 0 -> {
                            System.out.println("Goodbye!");
                            return;
                        }
                        default -> System.out.println("Invalid option.");
                    }
                } else {
                    System.out.println("\n=== Logged in as " + loggedInUser.getOwnerName() + " ===");
                    System.out.println("1. View Balance");
                    System.out.println("2. Transfer Money");
                    System.out.println("3. View Transactions");
                    System.out.println("4. Logout");
                    System.out.print("Choose an option: ");
                    int loggedInChoice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (loggedInChoice) {
                        case 1 -> System.out.println("Your balance: " + loggedInUser.getBalance());
                        case 2 -> {
                            System.out.print("Enter recipient account ID: ");
                            long toId = scanner.nextLong();
                            System.out.print("Enter amount: ");
                            BigDecimal amount = scanner.nextBigDecimal();
                            scanner.nextLine(); // consume newline
                            Account toAccount = accountService.findById(toId);
                            if (toAccount != null) {
                                Transaction tx = transactionService.transfer(loggedInUser, toAccount, amount);
                                System.out.println("Transaction status: " + tx.getStatus());
                            } else {
                                System.out.println("Recipient not found.");
                            }
                        }
                        case 3 -> {
                            List<Transaction> txs = transactionService.getTransactionsForAccount(loggedInUser);
                            for (Transaction tx : txs) {
                                System.out.println(tx);
                            }
                        }
                        case 4 -> {
                            loggedInUser = null;
                            System.out.println("Logged out successfully.");
                        }
                        default -> System.out.println("Invalid option.");
                    }
                }
            }
        }
    }
}
