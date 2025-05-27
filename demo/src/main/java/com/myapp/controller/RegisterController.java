package com.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myapp.model.Users;
import com.myapp.model.enums.Role;
import com.myapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                               @RequestParam String username,
                               @RequestParam String password,
                               Model model) {
        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "Bu kullanıcı adı zaten alınmış.");
            return "register";
        }
        Users user = new Users(name, username, passwordEncoder.encode(password), Role.USER);
        userRepository.save(user);
        // Kayıt başarılıysa login sayfasına yönlendir
        return "redirect:/login?success";
    }
}
