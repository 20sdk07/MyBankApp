package com.myapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.myapp.model.Account;
import com.myapp.service.AccountService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        String username = principal.getName();
        List<Account> accounts = accountService.getAccountsByOwnerName(username);
        BigDecimal totalBalance = accounts.stream()
            .map(Account::getBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("accounts", accounts); // <-- EKLE
        model.addAttribute("totalBalance", totalBalance);
        return "index"; // templates/index.html dosyasını gösterir
    }
}
