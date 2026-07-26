package com.spendwise.dsa;

import com.spendwise.model.Transaction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TransactionSearch {
    private TransactionSearch() {
    }

    public static List<Transaction> binarySearchByAmount(List<Transaction> transactions, BigDecimal amount) {
        List<Transaction> sorted = MergeSortUtil.sort(transactions, Comparator.comparing(Transaction::getAmount));
        List<Transaction> matches = new ArrayList<>();
        int low = 0;
        int high = sorted.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = sorted.get(mid).getAmount().compareTo(amount);
            if (comparison == 0) {
                collectEqualAmounts(sorted, amount, mid, matches);
                break;
            }
            if (comparison < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return matches;
    }

    private static void collectEqualAmounts(List<Transaction> sorted, BigDecimal amount, int index, List<Transaction> matches) {
        int left = index;
        while (left >= 0 && sorted.get(left).getAmount().compareTo(amount) == 0) {
            left--;
        }

        int right = index;
        while (right < sorted.size() && sorted.get(right).getAmount().compareTo(amount) == 0) {
            right++;
        }

        for (int current = left + 1; current < right; current++) {
            matches.add(sorted.get(current));
        }
    }
}

