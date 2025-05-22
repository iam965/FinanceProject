package com.financeproject.data.api

import com.financeproject.data.db.CurrencyEntity

data class Currency(
    val ID: String,
    val NumCode: String,
    val CharCode: String,
    val Nominal: Int,
    val Name: String,
    val Value: Double,
    val Previous: Double
) {
    fun toEntity(): CurrencyEntity {
        return CurrencyEntity(
            id = ID,
            numCode = NumCode,
            charCode = CharCode,
            nominal = Nominal,
            name = Name,
            value = Value,
            previous = Previous
        )
    }
}
