package com.spendwise.service;

import com.spendwise.dsa.TopCategoryHeap;
import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsService {
    public Map<String, BigDecimal> categoryTotals(List<Transaction> transactions) {
        Map<String, BigDecimal> totals = new HashMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                totals.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
            }
        }
        return totals;
    }

    public List<Map.Entry<String, BigDecimal>> topCategories(List<Transaction> transactions, int limit) {
        return TopCategoryHeap.topCategories(categoryTotals(transactions), limit);
    }

    public BigDecimal totalIncome(List<Transaction> transactions) {
        return totalByType(transactions, TransactionType.INCOME);
    }

    public BigDecimal totalExpenses(List<Transaction> transactions) {
        return totalByType(transactions, TransactionType.EXPENSE);
    }

    public String weekendInsight(List<Transaction> transactions) {
        BigDecimal weekend = BigDecimal.ZERO;
        BigDecimal weekday = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getType() != TransactionType.EXPENSE) {
                continue;
            }
            DayOfWeek day = transaction.getTransactionDate().getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                weekend = weekend.add(transaction.getAmount());
            } else {
                weekday = weekday.add(transaction.getAmount());
            }
        }
        return weekend.compareTo(weekday) > 0
                ? "Weekend spending is higher than weekday spending this month."
                : "Weekday spending is currently higher than weekend spending.";
    }

    private BigDecimal totalByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(transaction -> transaction.getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

