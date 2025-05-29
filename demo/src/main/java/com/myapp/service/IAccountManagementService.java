package com.myapp.service;

import java.math.BigDecimal;
import com.myapp.model.Account;

public interface IAccountManagementService {
    Account createAccount(String owner, BigDecimal initialBalance);
    void deleteAccount(long accountId);
}
