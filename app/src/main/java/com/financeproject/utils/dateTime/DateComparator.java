package com.financeproject.utils.dateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateComparator {
    public static int compareDate(LocalDateTime date1, LocalDateTime date2){
        return date1.compareTo(date2);
    }

    public static boolean isInMonth(LocalDateTime date, LocalDateTime month){
        return (date.getMonth() == month.getMonth() && date.getYear() == month.getYear());
    }

    public static boolean isInPeriod(LocalDate date, LocalDate b, LocalDate e){
        return (date.isAfter(b) || date.isEqual(b)) && (date.isBefore(e) || date.isEqual(e));
    }
}