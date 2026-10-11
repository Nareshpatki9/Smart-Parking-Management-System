package com.parking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    // =========================================================
    // PRIMARY KEY
    // =========================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================================================
    // USERNAME
    // =========================================================

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String username;

    // =========================================================
    // PASSWORD
    // Never expose password in JSON response
    // =========================================================

    @JsonIgnore
    @Column(
            nullable = false,
            length = 255
    )
    private String password;

    // =========================================================
    // ROLE
    // USER / ADMIN
    // =========================================================

    @Column(
            nullable = false,
            length = 20
    )
    private String role;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // Required by JPA
    // =========================================================

    public User() {
    }

    // =========================================================
    // PARAMETERIZED CONSTRUCTOR
    // =========================================================

    public User(
            String username,
            String password,
            String role) {

        this.username = username;
        this.password = password;
        this.role = role;
    }

    // =========================================================
    // GETTERS & SETTERS
    // =========================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}