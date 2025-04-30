package com.myapp.model;

import java.util.UUID;

import com.myapp.model.enums.Role;

public class User {
    private UUID id;
    private String username;
    private String password;
    private Role role;

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(String username, String password, Role role) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
