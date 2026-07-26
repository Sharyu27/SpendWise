package com.spendwise.dao;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> findByUserId(int userId) throws SQLException {
        String sql = """
                SELECT transaction_id, user_id, type, amount, merchant, item_name, category,
                       payment_method, notes, transaction_date
                FROM transactions
                WHERE user_id = ?
                ORDER BY transaction_date DESC, transaction_id DESC
                """;
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(mapTransaction(resultSet));
                }
            }
        }
        return transactions;
    }

    public Transaction save(Transaction transaction) throws SQLException {
        String sql = """
                INSERT INTO transactions
                (user_id, type, amount, merchant, item_name, category, payment_method, notes, transaction_date)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, transaction.getUserId());
            statement.setString(2, transaction.getType().name());
            statement.setBigDecimal(3, transaction.getAmount());
            statement.setString(4, transaction.getMerchant());
            statement.setString(5, transaction.getItemName());
            statement.setString(6, transaction.getCategory());
            statement.setString(7, transaction.getPaymentMethod());
            statement.setString(8, transaction.getNotes());
            statement.setDate(9, Date.valueOf(transaction.getTransactionDate()));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Transaction(
                            keys.getInt(1),
                            transaction.getUserId(),
                            transaction.getType(),
                            transaction.getAmount(),
                            transaction.getMerchant(),
                            transaction.getItemName(),
                            transaction.getCategory(),
                            transaction.getPaymentMethod(),
                            transaction.getNotes(),
                            transaction.getTransactionDate()
                    );
                }
            }
        }
        throw new SQLException("Transaction was not saved.");
    }

    public boolean delete(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transactionId);
            return statement.executeUpdate() > 0;
        }
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {
        return new Transaction(
                resultSet.getInt("transaction_id"),
                resultSet.getInt("user_id"),
                TransactionType.valueOf(resultSet.getString("type")),
                resultSet.getBigDecimal("amount"),
                resultSet.getString("merchant"),
                resultSet.getString("item_name"),
                resultSet.getString("category"),
                resultSet.getString("payment_method"),
                resultSet.getString("notes"),
                resultSet.getDate("transaction_date").toLocalDate()
        );
    }
}

