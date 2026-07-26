package com.spendwise.dsa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public final class TopCategoryHeap {
    private TopCategoryHeap() {
    }

    public static List<Map.Entry<String, BigDecimal>> topCategories(Map<String, BigDecimal> categoryTotals, int limit) {
        PriorityQueue<Map.Entry<String, BigDecimal>> heap = new PriorityQueue<>(
                Map.Entry.comparingByValue(Comparator.reverseOrder())
        );
        heap.addAll(categoryTotals.entrySet());

        List<Map.Entry<String, BigDecimal>> result = new ArrayList<>();
        while (!heap.isEmpty() && result.size() < limit) {
            result.add(heap.poll());
        }
        return result;
    }
}

