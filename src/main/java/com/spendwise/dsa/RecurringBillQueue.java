package com.spendwise.dsa;

import com.spendwise.model.RecurringBill;

import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

public class RecurringBillQueue {
    private final Queue<RecurringBill> bills = new PriorityQueue<>(Comparator.comparing(RecurringBill::getDueDate));

    public void add(RecurringBill bill) {
        bills.offer(bill);
    }

    public Optional<RecurringBill> nextDueBill() {
        return Optional.ofNullable(bills.peek());
    }
}

