package com.myapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.model.FreezeRequest;
import com.myapp.service.FreezeRequestService;

@RestController
@RequestMapping("/admin/freeze-requests")
@PreAuthorize("hasRole('ADMIN')") // Sadece ADMIN rolüne sahip kullanıcılar erişebilir
public class AdminController {

    private final FreezeRequestService freezeRequestService;

    public AdminController(FreezeRequestService freezeRequestService) {
        this.freezeRequestService = freezeRequestService;
    }

    @GetMapping
    public ResponseEntity<List<FreezeRequest>> getPendingFreezeRequests() {
        List<FreezeRequest> pendingRequests = freezeRequestService.getAllPendingFreezeRequests();
        return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<Void> approveFreezeRequest(@PathVariable Long requestId) {
        try {
            freezeRequestService.approveFreezeRequest(requestId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectFreezeRequest(@PathVariable Long requestId) {
        try {
            freezeRequestService.rejectFreezeRequest(requestId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}