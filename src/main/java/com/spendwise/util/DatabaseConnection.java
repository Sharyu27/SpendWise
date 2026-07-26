package com.spendwise.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/spendwise";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = readEnv("SPENDWISE_DB_URL", DEFAULT_URL);
        String user = readEnv("SPENDWISE_DB_USER", "root");
        String password = readEnv("SPENDWISE_DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean isAvailable() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (SQLException exception) {
            return false;
        }
    }

    private static String readEnv(String key, String fallback) {
        String value = System.getenv(key);
        return value == null || value.isBlank() ? fallback : value;
    }
}

