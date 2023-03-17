package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static final DateTimeFormatter MMYYYY_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy");
    public static final String NOW_STRING = "сейчас";

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static String toDisplayDateFrom(LocalDate date) {
        return date.format(MMYYYY_FORMAT);
    }

    public static String toDisplayDateTo(LocalDate date) {
        return (LocalDate.MAX.equals(date) ? NOW_STRING : date.format(MMYYYY_FORMAT));
    }
}

