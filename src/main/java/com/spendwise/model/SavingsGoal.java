package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SavingsGoal {
    private int goalId;
    private int userId;
    private String goalName;
    private BigDecimal targetAmount;
    private BigDecimal savedAmount;
    private LocalDate deadline;

    public SavingsGoal(int goalId, int userId, String goalName, BigDecimal targetAmount, BigDecimal savedAmount, LocalDate deadline) {
        this.goalId = goalId;
        this.userId = userId;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
    }

    public int getGoalId() {
        return goalId;
    }

    public int getUserId() {
        return userId;
    }

    public String getGoalName() {
        return goalName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getSavedAmount() {
        return savedAmount;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
}

