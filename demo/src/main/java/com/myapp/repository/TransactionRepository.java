
package com.myapp.repository;

import java.util.ArrayList;
import java.util.List;

import com.myapp.model.Transaction;

public class TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> findAll() {
        return transactions;
    }

    public List<Transaction> findByAccountId(int accountId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction tx : transactions) {
            if (tx.getFromAccount().getId() == accountId || tx.getToAccount().getId() == accountId) {
                result.add(tx);
            }
        }
        return result;
    }
}
