package com.myapp.model;

import java.math.BigDecimal;

import com.myapp.model.enums.FreezeStatus;

public class Account {
    private int id;
    private String ownerName;
    private BigDecimal balance;
    private boolean isDeleted = false;
    private FreezeStatus freezeStatus = FreezeStatus.ACTIVE; // Default olarak aktif



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public FreezeStatus getFreezeStatus() {
        return freezeStatus;
    }

    public void setFreezeStatus(FreezeStatus freezeStatus) {
        this.freezeStatus = freezeStatus;
    }
    
}
