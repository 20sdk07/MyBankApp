package com.myapp.model;

import java.math.BigDecimal;
import com.myapp.model.enums.FreezeStatus;
import jakarta.persistence.*;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ownerName;
    private BigDecimal balance;
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private FreezeStatus freezeStatus = FreezeStatus.ACTIVE; // Default olarak aktif

    // Getters
    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public FreezeStatus getFreezeStatus() {
        return freezeStatus;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setFreezeStatus(FreezeStatus freezeStatus) {
        this.freezeStatus = freezeStatus;
    }
}
