package com.myapp.exception;

public class InsufficientBalanceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final Long accountId;
    private final java.math.BigDecimal requestedAmount;
    private final java.math.BigDecimal availableBalance;

    public InsufficientBalanceException(String message, Long accountId, java.math.BigDecimal requestedAmount, java.math.BigDecimal availableBalance) {
        super(message);
        this.accountId = accountId;
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public Long getAccountId() {
        return accountId;
    }

    public java.math.BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public java.math.BigDecimal getAvailableBalance() {
        return availableBalance;
    }
}
