package com.urise.webapp.util;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {
    public static final DateTimeFormatter MMYYYY_FORMAT = DateTimeFormatter.ofPattern("MM/yyyy");
    public static final String NOW_STRING = "сейчас";

    public static LocalDate of(int year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate ofBegMon(String inputDate) {
        return YearMonth.parse(inputDate, MMYYYY_FORMAT).atDay(1);
    }

    public static LocalDate ofEndMon(String inputDate) {
        if (inputDate.trim().isEmpty() || inputDate.trim().toLowerCase().contains("сей")) {
            return LocalDate.MAX;
        } else {
            return YearMonth.parse(inputDate, MMYYYY_FORMAT).atEndOfMonth();
        }
    }

    public static LocalDate lastDayOf(int year, int month) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
    }

    public static String toDisplayDateFrom(LocalDate date) {
        return date.format(MMYYYY_FORMAT);
    }

    public static String toDisplayDateTo(LocalDate date) {
        return (LocalDate.MAX.equals(date) ? NOW_STRING : date.format(MMYYYY_FORMAT));
    }

    public static String toEditDate(LocalDate date) {
        return (LocalDate.MAX.equals(date)) ? NOW_STRING : date.format(MMYYYY_FORMAT);
    }
}

