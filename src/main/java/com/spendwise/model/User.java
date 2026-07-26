package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private BigDecimal monthlyBudget;
    private LocalDateTime createdAt;

    public User(int userId, String name, String email, String password, BigDecimal monthlyBudget, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.monthlyBudget = monthlyBudget;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

