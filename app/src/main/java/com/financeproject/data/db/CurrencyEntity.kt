package com.financeproject.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.financeproject.data.api.Currency

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val id: String,
    val numCode: String,
    val charCode: String,
    val name: String,
    val nominal: Int,
    val value: Double,
    val previous: Double
) {
    fun toCurrency(): Currency {
        return Currency(
            ID = id,
            NumCode = numCode,
            CharCode = charCode,
            Name = name,
            Nominal = nominal,
            Value = value,
            Previous = previous
        )
    }
}
