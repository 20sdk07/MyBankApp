package com.myapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Sdkral2055"; // Şifreni buraya yaz
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Bcrypt hash: " + encodedPassword);
    }
}