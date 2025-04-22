package com.myapp;


import java.util.Scanner;

import com.myapp.controller.AccountController;
import com.myapp.repository.AccountRepository;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in) ) {
            // Repository'ler
            AccountRepository accountRepository = new AccountRepository();
            TransactionRepository transactionRepository = new TransactionRepository();

            // Service'ler
            AccountService accountService = new AccountService(accountRepository);
            TransactionService transactionService = new TransactionService(transactionRepository);

            // Controller
            AccountController controller = new AccountController(accountService, transactionService);

            // Uygulamayı çalıştır
            controller.start();
        } 
        
    }
}
