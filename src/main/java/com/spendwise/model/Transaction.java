package com.spendwise.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int userId;
    private TransactionType type;
    private BigDecimal amount;
    private String merchant;
    private String itemName;
    private String category;
    private String paymentMethod;
    private String notes;
    private LocalDate transactionDate;

    public Transaction(
            int transactionId,
            int userId,
            TransactionType type,
            BigDecimal amount,
            String merchant,
            String itemName,
            String category,
            String paymentMethod,
            String notes,
            LocalDate transactionDate
    ) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.merchant = merchant;
        this.itemName = itemName;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }
}

