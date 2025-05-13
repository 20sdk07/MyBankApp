package com.myapp.model;

import java.util.UUID;

import com.myapp.model.enums.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Users {
    
    @Id
    private UUID id;
    private String name;
    private String username;
    private String password;
    private Role role;

    public Users() {
        this.id = UUID.randomUUID();
    }

    public Users(String name, String username, String password, Role role ) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
