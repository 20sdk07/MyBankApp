package com.myapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myapp.service.FreezeRequestService;

@Controller
@RequestMapping("/admin/freeze-requests")
@PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN rolüne sahip kullanıcılar erişebilir
public class AdminController {

    private final FreezeRequestService freezeRequestService;

    public AdminController(FreezeRequestService freezeRequestService) {
        this.freezeRequestService = freezeRequestService;
    }

    @GetMapping
    public String getPendingFreezeRequests(Model model) {
        model.addAttribute("requests", freezeRequestService.getAllPendingFreezeRequests());
        return "admin/freeze-requests"; // admin/freeze-requests.html'i göster
    }

    @PostMapping("/{requestId}/approve")
    public String approveFreezeRequest(@PathVariable Long requestId, Model model) {
        try {
            freezeRequestService.approveFreezeRequest(requestId);
            return "redirect:/admin/freeze-requests"; // İstek listesine geri yönlendir
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/freeze-requests"; // Hata varsa aynı sayfada göster
        }
    }

    @PostMapping("/{requestId}/reject")
    public String rejectFreezeRequest(@PathVariable Long requestId, Model model) {
        try {
            freezeRequestService.rejectFreezeRequest(requestId);
            return "redirect:/admin/freeze-requests"; // İstek listesine geri yönlendir
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "admin/freeze-requests"; // Hata varsa aynı sayfada göster
        }
    }
}
