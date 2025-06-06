package com.myapp.repository;

import com.myapp.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.id = :accountId OR t.toAccount.id = :accountId " +
           "ORDER BY t.timestamp DESC")
    List<Transaction> findByFromAccount_IdOrToAccount_Id(@Param("accountId") Long accountId, @Param("accountId") Long sameAccountId);
}
