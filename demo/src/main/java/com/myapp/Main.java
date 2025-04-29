package com.myapp;

import java.util.Scanner;

import com.myapp.controller.AccountController;
import com.myapp.repository.AccountRepository;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
        // Repository'ler
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Service'ler
        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService();

        // Controller
        AccountController controller = new AccountController(accountService, transactionService);

        // Uygulamayı çalıştır
        controller.start();
        
        } 
        
        finally {
            scanner.close(); // Scanner'ı uygulama tamamen bittikten sonra kapatın
        }
    }
}