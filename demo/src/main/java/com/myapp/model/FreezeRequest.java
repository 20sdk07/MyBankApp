package com.myapp.model;

import java.time.LocalDateTime;

import com.myapp.model.enums.FreezeRequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FreezeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    private FreezeRequestStatus status;

    // Getter ve Setter metotları

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public FreezeRequestStatus getStatus() {
        return status;
    }

    public void setStatus(FreezeRequestStatus status) {
        this.status = status;
    }
}