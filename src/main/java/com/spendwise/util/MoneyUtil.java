package com.spendwise.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class MoneyUtil {
    private static final NumberFormat FORMATTER = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    private MoneyUtil() {
    }

    public static String format(BigDecimal amount) {
        return FORMATTER.format(amount == null ? BigDecimal.ZERO : amount);
    }
}

