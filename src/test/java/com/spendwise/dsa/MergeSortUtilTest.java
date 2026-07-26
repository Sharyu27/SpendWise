package com.spendwise.dsa;

import com.spendwise.model.Transaction;
import com.spendwise.model.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MergeSortUtilTest {
    @Test
    void sortsTransactionsByAmount() {
        List<Transaction> sorted = MergeSortUtil.sort(List.of(
                transaction("300"),
                transaction("100"),
                transaction("200")
        ), Comparator.comparing(Transaction::getAmount));

        assertEquals(new BigDecimal("100"), sorted.get(0).getAmount());
        assertEquals(new BigDecimal("200"), sorted.get(1).getAmount());
        assertEquals(new BigDecimal("300"), sorted.get(2).getAmount());
    }

    private Transaction transaction(String amount) {
        return new Transaction(0, 1, TransactionType.EXPENSE, new BigDecimal(amount), "Test", "Item", "Food", "UPI", "", LocalDate.now());
    }
}

