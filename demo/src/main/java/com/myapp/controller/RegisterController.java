package com.myapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myapp.model.enums.Role;
import com.myapp.repository.UserRepository;
import com.myapp.service.UserService;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final UserService userService;

    public RegisterController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
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
        userService.createUser(username, name, password, Role.USER);
        return "redirect:/login?success";
    }
}
