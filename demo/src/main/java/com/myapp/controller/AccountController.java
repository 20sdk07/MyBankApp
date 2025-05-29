package com.myapp.controller;

import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myapp.model.Account;
import com.myapp.exception.AccountFrozenException;
import com.myapp.exception.InsufficientBalanceException;
import com.myapp.model.enums.FreezeStatus;
import com.myapp.service.IAccountManagementService;
import com.myapp.service.IAccountQueryService;
import com.myapp.service.TransactionService;
import com.myapp.service.FreezeRequestService;
import java.util.Optional;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final IAccountManagementService accountManagementService;
    private final IAccountQueryService accountQueryService;
    private final TransactionService transactionService;
    private final FreezeRequestService freezeRequestService;

    public AccountController(IAccountManagementService accountManagementService,
                           IAccountQueryService accountQueryService,
                           TransactionService transactionService,
                           FreezeRequestService freezeRequestService) {
        this.accountManagementService = accountManagementService;
        this.accountQueryService = accountQueryService;
        this.transactionService = transactionService;
        this.freezeRequestService = freezeRequestService;
    }

    @GetMapping
    public String listAccounts(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("accounts", accountQueryService.getAccountsByOwnerName(username));
        return "accounts";
    }

    @GetMapping("/new")
    public String showCreateAccountForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("username", principal.getName());
        return "create-account";
    }

    @PostMapping
    public String createAccount(@RequestParam String ownerName,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        if (principal == null || !principal.getName().equals(ownerName)) {
            logger.warn("Authorization error: Principal name does not match owner name. Principal: {}, Owner: {}", 
                       principal != null ? principal.getName() : "null", ownerName);
            redirectAttributes.addFlashAttribute("error", "Yetkilendirme hatası: Geçersiz hesap sahibi");
            return "redirect:/accounts";
        }

        try {
            logger.info("Attempting to create account for user: {}", ownerName);
            Account account = accountManagementService.createAccount(ownerName, new BigDecimal("1000.00"));
            logger.info("Successfully created account ID: {} for user: {}", account.getId(), ownerName);
            
            redirectAttributes.addFlashAttribute("success", 
                String.format("Hesap başarıyla oluşturuldu. Hesap No: %d. Başlangıç bakiyesi: 1.000,00 ₺", account.getId()));
            return "redirect:/accounts";
        } catch (Exception e) {
            logger.error("Failed to create account for user: {}", ownerName, e);
            redirectAttributes.addFlashAttribute("error", "Hesap oluşturulamadı: " + e.getMessage());
            return "redirect:/accounts/new";
        }
    }

    @GetMapping("/{id}")
    public String getAccount(@PathVariable long id, Model model, Principal principal) {
        Optional<Account> account = accountQueryService.findById(id);
        if (account.isPresent() && account.get().getOwnerName().equals(principal.getName())) {
            Account acc = account.get();
            model.addAttribute("account", acc);
            model.addAttribute("transactions", transactionService.getTransactionsForAccount(acc));
            model.addAttribute("freezeRequestExists", freezeRequestService.existsPendingRequestForAccount(id));
            return "account-detail";
        }
        return "redirect:/accounts?error=Hesap bulunamadı veya yetkisiz erişim";
    }

    @PostMapping("/{id}/delete")
    public String deleteAccount(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            accountManagementService.deleteAccount(id);
            redirectAttributes.addFlashAttribute("success", "Hesap silindi.");
            return "redirect:/accounts";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/accounts";
        }
    }

    @PostMapping("/{id}/freeze-request")
    public String createFreezeRequest(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            if (freezeRequestService.createFreezeRequest(id)) {
                redirectAttributes.addFlashAttribute("success", "Dondurma isteği başarıyla oluşturuldu.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Bu hesap için zaten bekleyen bir dondurma isteği bulunmaktadır.");
            }
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Beklenmeyen bir hata oluştu: " + e.getMessage());
        }
        return "redirect:/accounts/" + id;
    }

    @GetMapping("/{id}/transfer")
    public String showTransferForm(@PathVariable long id, Model model, Principal principal) {
        Optional<Account> account = accountQueryService.findById(id);
        if (account.isPresent() && account.get().getOwnerName().equals(principal.getName())) {            Account acc = account.get();
            // Dondurulmuş hesap kontrolü
            if (acc.getFreezeStatus() == FreezeStatus.FROZEN) {
                model.addAttribute("account", acc);
                model.addAttribute("error", "Bu hesap dondurulmuş durumda olduğu için transfer işlemi yapamazsınız.");
                return "transfer";
            }
            model.addAttribute("account", acc);
            return "transfer";
        }
        return "redirect:/accounts?error=Hesap bulunamadı veya yetkisiz erişim";
    }    @PostMapping("/{id}/transfer")
    public String processTransfer(@PathVariable long id,
                                @RequestParam Long targetAccountId,
                                @RequestParam BigDecimal amount,
                                RedirectAttributes redirectAttributes,
                                Principal principal) {
        Optional<Account> account = accountQueryService.findById(id);
        if (account.isPresent() && account.get().getOwnerName().equals(principal.getName())) {
            Account sourceAccount = account.get();
            
            // Kaynak hesap dondurulmuş mu kontrol et
            if (sourceAccount.getFreezeStatus() == FreezeStatus.FROZEN) {
                redirectAttributes.addFlashAttribute("error", 
                    "Bu hesap dondurulmuş durumda olduğu için transfer yapılamaz.");
                return "redirect:/accounts/" + id;
            }
            
            // Hedef hesap dondurulmuş mu kontrol et
            Optional<Account> targetAccount = accountQueryService.findById(targetAccountId);
            if (targetAccount.isPresent() && targetAccount.get().getFreezeStatus() == FreezeStatus.FROZEN) {
                redirectAttributes.addFlashAttribute("error", 
                    "Hedef hesap dondurulmuş durumda olduğu için transfer yapılamaz.");
                return "redirect:/accounts/" + id + "/transfer";
            }            try {
                transactionService.transfer(id, targetAccountId, amount);
                redirectAttributes.addFlashAttribute("success", "Transfer başarıyla gerçekleştirildi.");
                return "redirect:/accounts/" + id;
            } catch (AccountFrozenException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/accounts/" + id;
            } catch (InsufficientBalanceException e) {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:/accounts/" + id + "/transfer";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Transfer başarısız: " + e.getMessage());
                return "redirect:/accounts/" + id + "/transfer";
            }
        }
        redirectAttributes.addFlashAttribute("error", "Hesap bulunamadı veya yetkisiz erişim");
        return "redirect:/accounts";
    }
}
