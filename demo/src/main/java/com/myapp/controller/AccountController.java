package com.myapp.controller;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myapp.model.Account;
import com.myapp.service.AccountService;
import com.myapp.service.FreezeRequestService;
import com.myapp.service.TransactionService;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final FreezeRequestService freezeRequestService;

    public AccountController(AccountService accountService, TransactionService transactionService, FreezeRequestService freezeRequestService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.freezeRequestService = freezeRequestService;
    }

    @GetMapping
    public String listAccounts(Model model) {
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "accounts";
    }

    @GetMapping("/create-account")
    public String showCreateAccountForm() {
        return "create-account";
    }

    @PostMapping
    public String createAccount(@RequestParam String ownerName, @RequestParam BigDecimal initialBalance) {
        accountService.createAccount(ownerName, initialBalance);
        return "redirect:/accounts";
    }

    @GetMapping("/{accountId}")
    public String getAccount(@PathVariable long accountId, Model model) {
        Optional<Account> account = accountService.findById(accountId);
        if (account.isPresent()) {
            model.addAttribute("account", account.get());
            return "account-info";
        } else {
            return "redirect:/accounts?error=Hesap Bulunamadı"; // Redirect ile hata mesajı
        }
    }



    @PostMapping("/transfer")
    public String transfer(@RequestParam Long fromAccountId, @RequestParam Long toAccountId, @RequestParam BigDecimal amount, Model model) {
        try {
            transactionService.transfer(fromAccountId, toAccountId, amount); // Doğrudan servis katmanını çağırıyoruz.
            return "redirect:/accounts"; // Başarılıysa hesap listesine dön
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "transfer-form"; // Hata durumunda transfer formuna geri dön ve hatayı göster
        }
    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model){
        model.addAttribute("accounts", accountService.getAllAccounts());
        return "transfer-form";
    }


    @GetMapping("/{accountId}/info")
    public String viewAccountInfo(@PathVariable long accountId, Model model) {
        Optional<Account> account = accountService.findById(accountId);
        if (account.isPresent()) {
            model.addAttribute("account", account.get());
            return "account-info";
        } else {
            return "redirect:/accounts?error=Hesap Bulunamadı";
        }
    }

    @PostMapping("/{accountId}/freeze-request")
    public String requestFreezeAccount(@PathVariable Long accountId) {
        try {
            freezeRequestService.createFreezeRequest(accountId);
            return "redirect:/accounts";
        } catch (IllegalArgumentException e) {
            return "redirect:/accounts?error=" + e.getMessage();
        }
    }

    @PostMapping("/{accountId}/freeze")
    public String freezeAccount(@PathVariable long accountId) {
        try {
            accountService.freezeAccount(accountId);
            return "redirect:/accounts";
        } catch (IllegalArgumentException e) {
            return "redirect:/accounts?error=" + e.getMessage();
        }
    }

    @PostMapping("/{accountId}/unfreeze")
    public String unfreezeAccount(@PathVariable long accountId) {
        try {
            accountService.unfreezeAccount(accountId);
            return "redirect:/accounts";
        } catch (IllegalArgumentException e) {
           return "redirect:/accounts?error=" + e.getMessage();
        }
    }

    @PostMapping("/{accountId}/delete")
    public String deleteAccount(@PathVariable long accountId) {
        try {
            accountService.deleteAccount(accountId);
            return "redirect:/accounts";
        } catch (IllegalArgumentException e) {
            return "redirect:/accounts?error=" + e.getMessage();
        }
    }
}
