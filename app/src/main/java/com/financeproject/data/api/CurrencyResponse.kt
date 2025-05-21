package com.financeproject.data.api

data class CurrencyResponse(
    val Date: String,
    val PreviousDate: String,
    val PreviousURL: String,
    val Timestamp: String,
    val Valute: Map<String, Currency>
)
