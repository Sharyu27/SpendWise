package com.spendwise.model;

import java.math.BigDecimal;

public class Budget {
    private int budgetId;
    private int userId;
    private String month;
    private String category;
    private BigDecimal budgetAmount;

    public Budget(int budgetId, int userId, String month, String category, BigDecimal budgetAmount) {
        this.budgetId = budgetId;
        this.userId = userId;
        this.month = month;
        this.category = category;
        this.budgetAmount = budgetAmount;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMonth() {
        return month;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }
}

