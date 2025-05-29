package com.myapp.service;

import com.myapp.model.Users;
import com.myapp.model.enums.Role;
import com.myapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, AccountService accountService, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users createUser(String username, String name, String password, Role role) {
        Users user = new Users();
        user.setUsername(username);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role); // Burada role parametresi kullanılıyor
        userRepository.save(user);

        // Kullanıcıya otomatik hesap oluştur
        accountService.createAccount(username, new java.math.BigDecimal("1000.00"));

        return user;
    }

    public Optional<Users> findById(UUID userId) {
        return userRepository.findById(userId);
    }
}
