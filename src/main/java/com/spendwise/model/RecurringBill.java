package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecurringBill {
    private int billId;
    private int userId;
    private String billName;
    private BigDecimal amount;
    private LocalDate dueDate;
    private String category;

    public RecurringBill(int billId, int userId, String billName, BigDecimal amount, LocalDate dueDate, String category) {
        this.billId = billId;
        this.userId = userId;
        this.billName = billName;
        this.amount = amount;
        this.dueDate = dueDate;
        this.category = category;
    }

    public int getBillId() {
        return billId;
    }

    public int getUserId() {
        return userId;
    }

    public String getBillName() {
        return billName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getCategory() {
        return category;
    }
}

