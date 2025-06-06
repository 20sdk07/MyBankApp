package com.myapp.controller;

import java.math.BigDecimal;

public class TransferRequest {
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;

    // Getter ve Setter'lar
    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}