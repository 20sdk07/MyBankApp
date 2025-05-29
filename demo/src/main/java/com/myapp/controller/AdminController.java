package com.myapp.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myapp.model.Account;
import com.myapp.model.Transaction;
import com.myapp.model.Users;
import com.myapp.service.AdminService;
import com.myapp.service.UserService;
import com.myapp.service.FreezeRequestService;
import com.myapp.service.AccountQueryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final AdminService adminService;
    private final UserService userService;
    private final FreezeRequestService freezeRequestService;
    private final AccountQueryService accountQueryService;

    public AdminController(AdminService adminService, UserService userService, FreezeRequestService freezeRequestService, AccountQueryService accountQueryService) {
        this.adminService = adminService;
        this.userService = userService;
        this.freezeRequestService = freezeRequestService;
        this.accountQueryService = accountQueryService;
    }

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users-admin";
    }

    @GetMapping("/users/{userId}/accounts")
    public String listUserAccounts(@PathVariable UUID userId, Model model, RedirectAttributes redirectAttributes) {
        try {            Users user = adminService.getUserWithAccounts(userId);
            List<Account> accounts = adminService.getUserAccounts(userId);
            
            model.addAttribute("accounts", accounts);
            model.addAttribute("userId", userId);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userFullName", user.getName());
            return "admin/admin-user-accounts";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/accounts/{accountId}/details")
    public String viewAccountDetails(@PathVariable long accountId, Model model, RedirectAttributes redirectAttributes) {
        try {
            logger.info("Loading account details for account ID: {}", accountId);
            
            Account account = adminService.getAccountDetails(accountId);
            List<Transaction> transactions = adminService.getAccountTransactions(account);
            List<Account> validTransferAccounts = adminService.getValidTransferAccounts(accountId);
            
            model.addAttribute("account", account);
            model.addAttribute("transactions", transactions);
            model.addAttribute("accounts", validTransferAccounts);
            model.addAttribute("freezeRequestExists", adminService.hasPendingFreezeRequest(accountId));
            
            return "admin/account-details-admin";
        } catch (IllegalArgumentException e) {
            logger.error("Error loading account details for ID {}: {}", accountId, e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/dashboard";
        } catch (Exception e) {
            logger.error("Unexpected error loading account details for ID {}", accountId, e);
            redirectAttributes.addFlashAttribute("error", "Beklenmeyen bir hata oluştu. Lütfen tekrar deneyin.");
            return "redirect:/admin/dashboard";
        }
    }

    @PostMapping("/accounts/{accountId}/freeze-by-admin")
    public String freezeAccountByAdmin(@PathVariable long accountId, 
                                     @RequestParam(required = false) UUID userId,
                                     RedirectAttributes redirectAttributes) {
        try {
            adminService.freezeAccountByAdmin(accountId);
            redirectAttributes.addFlashAttribute("success", accountId + " numaralı hesap donduruldu.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return userId != null ? 
            "redirect:/admin/users/" + userId + "/accounts" : 
            "redirect:/admin/frozen-accounts";
    }

    @PostMapping("/accounts/{accountId}/unfreeze")
    public String unfreezeAccount(@PathVariable long accountId, 
                                @RequestParam(required = false) UUID userId,
                                RedirectAttributes redirectAttributes) {
        try {
            adminService.unfreezeAccount(accountId);
            redirectAttributes.addFlashAttribute("success", 
                accountId + " numaralı hesabın dondurulması kaldırıldı.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return userId != null ? 
            "redirect:/admin/users/" + userId + "/accounts" : 
            "redirect:/admin/frozen-accounts";
    }    @PostMapping("/accounts/{accountId}/delete")
    public String deleteAccount(@PathVariable long accountId, 
                              @RequestParam(required = false) UUID userId, 
                              RedirectAttributes redirectAttributes) {
        try {
            adminService.deleteAccount(accountId);
            redirectAttributes.addFlashAttribute("success", accountId + " numaralı hesap silindi.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return userId != null ? 
            "redirect:/admin/users/" + userId + "/accounts" : 
            "redirect:/admin/frozen-accounts";
    }

    @PostMapping("/accounts/{accountId}/transfer")
    public String adminTransfer(@PathVariable long accountId,
                              @RequestParam long targetAccountId,
                              @RequestParam java.math.BigDecimal amount,
                              RedirectAttributes redirectAttributes) {
        try {
            adminService.performAdminTransfer(accountId, targetAccountId, amount);
            redirectAttributes.addFlashAttribute("success", "Transfer başarıyla gerçekleştirildi.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Transfer başarısız: " + e.getMessage());
        }
        return "redirect:/admin/accounts/" + accountId + "/details";
    }

    @GetMapping("/frozen-accounts")
    public String listFrozenAccounts(Model model) {
        model.addAttribute("frozenAccounts", adminService.getFrozenAccounts());
        return "admin/frozen-accounts";
    }

    @GetMapping("/accounts/search")
    public String searchAccount(@RequestParam long accountId, RedirectAttributes redirectAttributes) {
        try {
            adminService.getAccountDetails(accountId); // Hesabın varlığını kontrol et
            return "redirect:/admin/accounts/" + accountId + "/details";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/dashboard";
        }
    }

    @GetMapping("/freeze-requests")
    public String listFreezeRequests(Model model) {
        model.addAttribute("requests", freezeRequestService.getAllPendingFreezeRequests());
        return "admin/freeze-requests";
    }

    @PostMapping("/freeze-requests/{id}/approve")
    public String approveFreezeRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            freezeRequestService.approveFreezeRequest(id);
            redirectAttributes.addFlashAttribute("success", "Dondurma isteği onaylandı.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/freeze-requests";
    }

    @PostMapping("/freeze-requests/{id}/reject")
    public String rejectFreezeRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            freezeRequestService.rejectFreezeRequest(id);
            redirectAttributes.addFlashAttribute("success", "Dondurma isteği reddedildi.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/freeze-requests";
    }
}
