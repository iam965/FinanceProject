package com.financeproject.logic.dateTime;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormat {
    private static final String dateFormat = "dd.MM.yy";
    private static final String dateTimeFormat = "dd.MM.yy HH:mm:ss";

    public static String getDateString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String getDateTimeString(LocalDateTime date){
        return date.format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDate getDateFromString(String date) throws ParseException {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static LocalDateTime getDateTimeFromString(String date) throws ParseException {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(dateTimeFormat));
    }
}