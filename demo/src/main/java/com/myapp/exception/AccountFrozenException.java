package com.myapp.exception;

public class AccountFrozenException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    private final Long accountId;

    public AccountFrozenException(String message, Long accountId) {
        super(message);
        this.accountId = accountId;
    }

    public Long getAccountId() {
        return accountId;
    }
}
