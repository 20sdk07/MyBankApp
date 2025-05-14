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

    public FreezeRequest createFreezeRequest(Long accountId) {
        Optional<Account> accountOptional = accountService.findById(accountId);
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("Belirtilen hesap bulunamadı: " + accountId);
        }
        Account account = accountOptional.get();

        FreezeRequest freezeRequest = new FreezeRequest();
        freezeRequest.setAccountId(accountId);
        freezeRequest.setRequestDate(LocalDateTime.now());
        freezeRequest.setStatus(FreezeRequestStatus.PENDING);

        return freezeRequestRepository.save(freezeRequest);
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

        accountService.freezeAccount(account.getId()); // AccountService'teki metodu kullanıyoruz
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
}