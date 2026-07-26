package com.spendwise.dao;

import com.spendwise.model.User;
import com.spendwise.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public class UserDAO {
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = """
                SELECT user_id, name, email, password, monthly_budget, created_at
                FROM users
                WHERE email = ?
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public User create(String name, String email, String password, BigDecimal monthlyBudget) throws SQLException {
        String sql = """
                INSERT INTO users (name, email, password, monthly_budget)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setBigDecimal(4, monthlyBudget);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new User(keys.getInt(1), name, email, password, monthlyBudget, LocalDateTime.now());
                }
            }
        }
        throw new SQLException("User was not created.");
    }

    private User mapUser(ResultSet resultSet) throws SQLException {
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getBigDecimal("monthly_budget"),
                createdAt == null ? LocalDateTime.now() : createdAt.toLocalDateTime()
        );
    }
}

