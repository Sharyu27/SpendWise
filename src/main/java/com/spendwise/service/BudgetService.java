package com.spendwise.service;

import com.spendwise.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class BudgetService {
    private final AnalyticsService analyticsService;

    public BudgetService(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    public BigDecimal remainingBudget(BigDecimal monthlyBudget, List<Transaction> transactions) {
        return monthlyBudget.subtract(analyticsService.totalExpenses(transactions));
    }

    public BigDecimal safeDailyLimit(BigDecimal monthlyBudget, List<Transaction> transactions) {
        BigDecimal remainingBudget = remainingBudget(monthlyBudget, transactions);
        int daysLeft = Math.max(1, YearMonth.now().lengthOfMonth() - LocalDate.now().getDayOfMonth() + 1);
        return remainingBudget.divide(BigDecimal.valueOf(daysLeft), 2, RoundingMode.HALF_UP).max(BigDecimal.ZERO);
    }
}

