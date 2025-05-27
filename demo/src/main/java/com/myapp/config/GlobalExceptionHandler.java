package com.myapp.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        ex.printStackTrace(); // Konsola tam stack trace yaz
        model.addAttribute("errorMessage", ex.getMessage());
        return "error"; // src/main/resources/templates/error.html olmalı
    }
}