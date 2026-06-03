package com.raihan.nexaorder.util;

import java.time.YearMonth;
import java.time.format.DateTimeParseException;

public final class DateFilterUtil {

    private DateFilterUtil() {
    }

    public static YearMonth parseYearMonth(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return YearMonth.parse(value.trim());
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Month filter is invalid");
        }
    }

    public static String toMonthInputValue(YearMonth yearMonth) {
        return yearMonth == null ? "" : yearMonth.toString();
    }
}
