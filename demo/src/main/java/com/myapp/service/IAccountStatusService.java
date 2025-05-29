package com.myapp.service;

public interface IAccountStatusService {
    void freezeAccount(long accountId);
    void unfreezeAccount(long accountId);
}
