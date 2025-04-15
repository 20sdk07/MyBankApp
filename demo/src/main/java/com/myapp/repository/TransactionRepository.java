package com.myapp.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.myapp.model.Transaction;

public class TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    public Optional<Transaction> findById(long id) {
        return transactions.stream()
                .filter(transaction -> transaction.getId() == id)
                .findFirst();
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public void deleteById(long id) {
        transactions.removeIf(transaction -> transaction.getId() == id);
    }
}

