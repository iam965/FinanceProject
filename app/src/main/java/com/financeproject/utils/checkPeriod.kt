package com.financeproject.utils

import com.financeproject.data.db.Operation
import com.financeproject.utils.dateTime.DateComparator
import com.financeproject.utils.dateTime.DateFormat
import java.time.LocalDate

fun checkPeriod(beg: LocalDate, end: LocalDate, allOperations: List<Operation>): List<Operation> {
    val periodOperations = emptyList<Operation>().toMutableList()
    for (entry in allOperations) {
        if (DateComparator.isInPeriod(
                DateFormat.getDateFromString(entry.date), beg, end)){
            periodOperations += entry
        }
    }
    return periodOperations
}