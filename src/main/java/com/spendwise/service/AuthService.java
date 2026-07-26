package com.spendwise.service;

import com.spendwise.dao.UserDAO;
import com.spendwise.model.User;
import com.spendwise.util.DatabaseConnection;
import com.spendwise.util.InputValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private final DemoDataStore demoDataStore;

    public AuthService(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    public Optional<User> login(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }

        if (DatabaseConnection.isAvailable()) {
            try {
                return userDAO.findByEmail(email)
                        .filter(user -> user.getPassword().equals(password));
            } catch (SQLException ignored) {
                return demoLogin(email, password);
            }
        }
        return demoLogin(email, password);
    }

    public User register(String name, String email, String password, BigDecimal monthlyBudget) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Enter a valid email address.");
        }
        if (!InputValidator.isStrongPassword(password)) {
            throw new IllegalArgumentException("Password must have 8 characters, 1 uppercase letter, and 1 number.");
        }

        BigDecimal budget = monthlyBudget == null ? BigDecimal.ZERO : monthlyBudget;
        if (DatabaseConnection.isAvailable()) {
            try {
                return userDAO.create(name.trim(), email.trim(), password, budget);
            } catch (SQLException exception) {
                throw new IllegalArgumentException("Could not register user: " + exception.getMessage());
            }
        }
        return new User(2, name.trim(), email.trim(), password, budget, java.time.LocalDateTime.now());
    }

    private Optional<User> demoLogin(String email, String password) {
        User demo = demoDataStore.getDemoUser();
        if (demo.getEmail().equalsIgnoreCase(email.trim()) && demo.getPassword().equals(password)) {
            return Optional.of(demo);
        }
        return Optional.empty();
    }
}

