package com.myapp.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.myapp.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerName(String ownerName);
}
