package com.spendwise.service;

import com.spendwise.model.RecurringBill;
import com.spendwise.model.SavingsGoal;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DemoDataStore {
    private final User demoUser = new User(
            1,
            "Sharyu",
            "demo@spendwise.app",
            "Demo1234",
            new BigDecimal("10000.00"),
            LocalDateTime.now()
    );
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<SavingsGoal> goals = new ArrayList<>();
    private final List<RecurringBill> bills = new ArrayList<>();
    private int nextTransactionId = 100;

    public DemoDataStore() {
        transactions.add(new Transaction(1, 1, TransactionType.INCOME, new BigDecimal("15000"), null, null, "Pocket Money", "Bank Transfer", "Monthly allowance", LocalDate.now()));
        transactions.add(new Transaction(2, 1, TransactionType.EXPENSE, new BigDecimal("120"), "College Canteen", "Cold Coffee", "Food", "UPI", "After class", LocalDate.now()));
        transactions.add(new Transaction(3, 1, TransactionType.EXPENSE, new BigDecimal("220"), "Stationery Shop", "DSA Notebook", "Education", "UPI", "Algorithms notes", LocalDate.now()));
        transactions.add(new Transaction(4, 1, TransactionType.EXPENSE, new BigDecimal("340"), "D-Mart", "Milk and Bread", "Groceries", "Card", "Weekly essentials", LocalDate.now().minusDays(1)));
        transactions.add(new Transaction(5, 1, TransactionType.EXPENSE, new BigDecimal("180"), "Metro", "Student pass recharge", "Travel", "UPI", "Commute", LocalDate.now().minusDays(2)));
        transactions.add(new Transaction(6, 1, TransactionType.EXPENSE, new BigDecimal("850"), "Amazon", "Java Book", "Education", "UPI", "Interview prep", LocalDate.now().minusDays(4)));

        goals.add(new SavingsGoal(1, 1, "Laptop", new BigDecimal("65000"), new BigDecimal("42000"), LocalDate.now().plusDays(85)));
        bills.add(new RecurringBill(1, 1, "Mobile Recharge", new BigDecimal("299"), LocalDate.now().plusDays(5), "Bills"));
    }

    public User getDemoUser() {
        return demoUser;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<SavingsGoal> getGoals() {
        return goals;
    }

    public List<RecurringBill> getBills() {
        return bills;
    }

    public Transaction addTransaction(Transaction transaction) {
        Transaction saved = new Transaction(
                nextTransactionId++,
                demoUser.getUserId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getMerchant(),
                transaction.getItemName(),
                transaction.getCategory(),
                transaction.getPaymentMethod(),
                transaction.getNotes(),
                transaction.getTransactionDate()
        );
        transactions.add(0, saved);
        return saved;
    }

    public void removeTransaction(Transaction transaction) {
        transactions.removeIf(item -> item.getTransactionId() == transaction.getTransactionId());
    }
}

