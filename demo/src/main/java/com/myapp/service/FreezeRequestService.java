package com.myapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myapp.model.Account;
import com.myapp.model.FreezeRequest;
import com.myapp.model.enums.FreezeRequestStatus;
import com.myapp.repository.FreezeRequestRepository;

@Service
public class FreezeRequestService {

    private final FreezeRequestRepository freezeRequestRepository;
    private final AccountService accountService;

    public FreezeRequestService(FreezeRequestRepository freezeRequestRepository, AccountService accountService) {
        this.freezeRequestRepository = freezeRequestRepository;
        this.accountService = accountService;
    }

    public boolean createFreezeRequest(Long accountId) {
        if (existsPendingRequestForAccount(accountId)) {
            return false; // Zaten bekleyen istek var
        }
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("Belirtilen hesap bulunamadı: " + accountId);
        }
        Account account = accountOptional.get();

        // Admin hesabı kontrolü
        if (account.getOwnerName().equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("Admin hesabı için dondurma isteği oluşturulamaz!");
        }

        FreezeRequest freezeRequest = new FreezeRequest();
        freezeRequest.setAccountId(accountId);
        freezeRequest.setRequestDate(LocalDateTime.now());
        freezeRequest.setStatus(FreezeRequestStatus.PENDING);

        freezeRequestRepository.save(freezeRequest);
        return true;
    }

    public List<FreezeRequest> getAllPendingFreezeRequests() {
        return freezeRequestRepository.findByStatus(FreezeRequestStatus.PENDING);
    }

    @Transactional
    public void approveFreezeRequest(Long requestId) {
        Optional<FreezeRequest> freezeRequestOptional = freezeRequestRepository.findById(requestId);
        if (!freezeRequestOptional.isPresent()) {
            throw new IllegalArgumentException("Belirtilen dondurma isteği bulunamadı: " + requestId);
        }
        FreezeRequest freezeRequest = freezeRequestOptional.get();
        if (freezeRequest.getStatus() != FreezeRequestStatus.PENDING) {
            throw new IllegalArgumentException("Bu dondurma isteği zaten işlenmiş: " + requestId);
        }

        Optional<Account> accountOptional = accountService.findById(freezeRequest.getAccountId());
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("İlgili hesap bulunamadı: " + freezeRequest.getAccountId());
        }
        Account account = accountOptional.get();

        accountService.freezeAccount(account.getId());
        freezeRequest.setStatus(FreezeRequestStatus.APPROVED);
        freezeRequestRepository.save(freezeRequest);
    }

    public void rejectFreezeRequest(Long requestId) {
        Optional<FreezeRequest> freezeRequestOptional = freezeRequestRepository.findById(requestId);
        if (!freezeRequestOptional.isPresent()) {
            throw new IllegalArgumentException("Belirtilen dondurma isteği bulunamadı: " + requestId);
        }
        FreezeRequest freezeRequest = freezeRequestOptional.get();
        if (freezeRequest.getStatus() != FreezeRequestStatus.PENDING) {
            throw new IllegalArgumentException("Bu dondurma isteği zaten işlenmiş: " + requestId);
        }
        freezeRequest.setStatus(FreezeRequestStatus.REJECTED);
        freezeRequestRepository.save(freezeRequest);
    }

    public boolean existsPendingRequestForAccount(Long accountId) {
        return freezeRequestRepository.findByStatus(FreezeRequestStatus.PENDING)
            .stream()
            .anyMatch(req -> req.getAccountId().equals(accountId));
    }
}