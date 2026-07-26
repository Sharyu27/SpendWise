package com.spendwise.dsa;

import com.spendwise.model.Transaction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MergeSortUtil {
    private MergeSortUtil() {
    }

    public static List<Transaction> sort(List<Transaction> transactions, Comparator<Transaction> comparator) {
        if (transactions.size() <= 1) {
            return new ArrayList<>(transactions);
        }

        int middle = transactions.size() / 2;
        List<Transaction> left = sort(transactions.subList(0, middle), comparator);
        List<Transaction> right = sort(transactions.subList(middle, transactions.size()), comparator);
        return merge(left, right, comparator);
    }

    private static List<Transaction> merge(List<Transaction> left, List<Transaction> right, Comparator<Transaction> comparator) {
        List<Transaction> result = new ArrayList<>();
        int leftIndex = 0;
        int rightIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (comparator.compare(left.get(leftIndex), right.get(rightIndex)) <= 0) {
                result.add(left.get(leftIndex++));
            } else {
                result.add(right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            result.add(left.get(leftIndex++));
        }
        while (rightIndex < right.size()) {
            result.add(right.get(rightIndex++));
        }
        return result;
    }
}

