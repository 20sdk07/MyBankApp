package com.myapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapp.model.Users;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
}
