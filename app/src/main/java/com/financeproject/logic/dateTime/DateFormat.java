package com.financeproject.logic.dateTime;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    private static final String dateFormat = "dd.MM.yy";

    public static String getDateString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDate getDateFromString(String date) throws ParseException {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static long getMillisFromDate(LocalDate date){
        return date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
    }

    public static LocalDate getDateFromMillis(long mil){
        return Instant.ofEpochMilli(mil).atOffset(ZoneOffset.UTC).toLocalDate();
    }

    public static LocalDate getStartOfDay(LocalDate day){
        return day;
    }

    public static LocalDate getEndOfDay(LocalDate day){
        return day;
    }
}