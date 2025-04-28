package com.myapp;

import com.myapp.controller.AccountController;
import com.myapp.repository.AccountRepository;
import com.myapp.repository.TransactionRepository;
import com.myapp.service.AccountService;
import com.myapp.service.TransactionService;

public class Main {
    public static void main(String[] args) {

        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        AccountService accountService = new AccountService(accountRepository);
        TransactionService transactionService = new TransactionService(transactionRepository);

        AccountController accountController = new AccountController(accountService, transactionService);

        ConsoleApp consoleApp = new ConsoleApp(accountController);
        consoleApp.start();
    }
}
