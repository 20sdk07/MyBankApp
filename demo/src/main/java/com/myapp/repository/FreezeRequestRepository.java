package com.myapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.model.FreezeRequest;
import com.myapp.model.enums.FreezeRequestStatus;

@Repository
public interface FreezeRequestRepository extends JpaRepository<FreezeRequest, Long> {
    List<FreezeRequest> findByStatus(FreezeRequestStatus status);
}