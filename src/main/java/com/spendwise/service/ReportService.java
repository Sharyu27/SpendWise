package com.spendwise.service;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import com.spendwise.util.MoneyUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    private final AnalyticsService analyticsService;

    public ReportService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public int financialScore(BigDecimal monthlyBudget, List<Transaction> transactions) {
        BigDecimal expenses = analyticsService.totalExpenses(transactions);
        if (monthlyBudget.compareTo(BigDecimal.ZERO) <= 0) {
            return 70;
        }
        double ratio = expenses.doubleValue() / monthlyBudget.doubleValue();
        int score = (int) Math.round(100 - Math.max(0, ratio - 0.65) * 90);
        return Math.max(35, Math.min(100, score));
    }

    public String expenseMemory(List<Transaction> transactions) {
        List<Transaction> expenses = transactions.stream()
                .filter(transaction -> transaction.getType() == TransactionType.EXPENSE)
                .toList();
        if (expenses.isEmpty()) {
            return "No expenses recorded yet. Add your first expense to build memory insights.";
        }

        String topMerchant = mode(expenses.stream().map(Transaction::getMerchant).filter(value -> value != null).toList());
        String topItem = mode(expenses.stream().map(Transaction::getItemName).filter(value -> value != null).toList());
        Transaction largest = expenses.stream()
                .max(Comparator.comparing(Transaction::getAmount))
                .orElse(expenses.get(0));

        return "%d transactions recorded. Most visited merchant: %s. Most purchased item: %s. Largest expense: %s - %s."
                .formatted(expenses.size(), topMerchant, topItem, largest.getItemName(), MoneyUtil.format(largest.getAmount()));
    }

    private String mode(List<String> values) {
        return values.stream()
                .collect(Collectors.groupingBy(value -> value, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Not available");
    }
}

