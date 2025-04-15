package com.myapp.model;

import java.math.BigDecimal;

import com.myapp.model.enums.AccountStatus;

public class Account {
    private int accountNumber;
    private BigDecimal balance;
    private AccountStatus status;

    public Account(int accountNumber, BigDecimal balance, AccountStatus status) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
