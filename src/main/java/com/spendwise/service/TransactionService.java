package com.spendwise.service;

import com.spendwise.dao.TransactionDAO;
import com.spendwise.dsa.MergeSortUtil;
import com.spendwise.dsa.TransactionSearch;
import com.spendwise.dsa.UndoDeleteStack;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.util.DatabaseConnection;
import com.spendwise.util.InputValidator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final DemoDataStore demoDataStore;
    private final UndoDeleteStack undoDeleteStack = new UndoDeleteStack();

    public TransactionService(DemoDataStore demoDataStore) {
        this.demoDataStore = demoDataStore;
    }

    public List<Transaction> getTransactions(int userId) {
        if (DatabaseConnection.isAvailable()) {
            try {
                return transactionDAO.findByUserId(userId);
            } catch (SQLException ignored) {
                return demoDataStore.getTransactions();
            }
        }
        return demoDataStore.getTransactions();
    }

    public Transaction addExpense(int userId, BigDecimal amount, String merchant, String itemName, String category, String paymentMethod, LocalDate date, String notes) {
        if (!InputValidator.isPositiveMoney(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (merchant == null || merchant.isBlank()) {
            throw new IllegalArgumentException("Merchant is required.");
        }
        if (itemName == null || itemName.isBlank()) {
            throw new IllegalArgumentException("Item purchased is required.");
        }
        Transaction transaction = new Transaction(0, userId, TransactionType.EXPENSE, amount, merchant.trim(), itemName.trim(), category, paymentMethod, notes, date);
        return save(transaction);
    }

    public Transaction addIncome(int userId, BigDecimal amount, String source, LocalDate date, String notes) {
        if (!InputValidator.isPositiveMoney(amount)) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        Transaction transaction = new Transaction(0, userId, TransactionType.INCOME, amount, source, null, "Income", "Bank Transfer", notes, date);
        return save(transaction);
    }

    public List<Transaction> recentTransactions(int userId, int limit) {
        return MergeSortUtil.sort(getTransactions(userId), Comparator.comparing(Transaction::getTransactionDate).reversed())
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Transaction> searchByText(int userId, String query) {
        String normalized = query == null ? "" : query.trim().toLowerCase();
        if (normalized.isBlank()) {
            return getTransactions(userId);
        }
        return getTransactions(userId).stream()
                .filter(transaction -> contains(transaction.getMerchant(), normalized)
                        || contains(transaction.getItemName(), normalized)
                        || contains(transaction.getCategory(), normalized)
                        || contains(transaction.getNotes(), normalized))
                .collect(Collectors.toList());
    }

    public List<Transaction> searchByAmount(int userId, BigDecimal amount) {
        return TransactionSearch.binarySearchByAmount(getTransactions(userId), amount);
    }

    public void delete(Transaction transaction) {
        undoDeleteStack.push(transaction);
        if (DatabaseConnection.isAvailable()) {
            try {
                transactionDAO.delete(transaction.getTransactionId());
                return;
            } catch (SQLException ignored) {
                demoDataStore.removeTransaction(transaction);
                return;
            }
        }
        demoDataStore.removeTransaction(transaction);
    }

    public Optional<Transaction> undoLastDelete() {
        Optional<Transaction> restored = undoDeleteStack.pop();
        restored.ifPresent(demoDataStore::addTransaction);
        return restored;
    }

    private Transaction save(Transaction transaction) {
        if (DatabaseConnection.isAvailable()) {
            try {
                return transactionDAO.save(transaction);
            } catch (SQLException ignored) {
                return demoDataStore.addTransaction(transaction);
            }
        }
        return demoDataStore.addTransaction(transaction);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}

