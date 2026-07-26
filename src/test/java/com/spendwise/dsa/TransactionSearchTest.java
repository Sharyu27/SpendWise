package com.spendwise.dsa;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionSearchTest {
    @Test
    void findsMatchingTransactionsByAmountUsingBinarySearch() {
        List<Transaction> matches = TransactionSearch.binarySearchByAmount(List.of(
                transaction("120"),
                transaction("220"),
                transaction("120")
        ), new BigDecimal("120"));

        assertEquals(2, matches.size());
    }

    private Transaction transaction(String amount) {
        return new Transaction(0, 1, TransactionType.EXPENSE, new BigDecimal(amount), "Test", "Item", "Food", "UPI", "", LocalDate.now());
    }
}

