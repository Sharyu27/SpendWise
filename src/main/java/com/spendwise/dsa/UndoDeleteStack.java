package com.spendwise.dsa;

import com.spendwise.model.Transaction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class UndoDeleteStack {
    private final Deque<Transaction> deletedTransactions = new ArrayDeque<>();

    public void push(Transaction transaction) {
        deletedTransactions.push(transaction);
    }

    public Optional<Transaction> pop() {
        if (deletedTransactions.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(deletedTransactions.pop());
    }
}

