package com.financeproject.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(currencies: List<CurrencyEntity>)

    @Query("SELECT * FROM currency WHERE charCode IN (:codes)")
    suspend fun getByCharCodes(codes: List<String>): List<CurrencyEntity>

    @Query("SELECT * FROM currency WHERE charCode = :code")
    suspend fun getCurrency(code: String): CurrencyEntity

    @Query("SELECT * FROM currency")
    suspend fun getAll(): List<CurrencyEntity>

    @Query("DELETE FROM currency")
    suspend fun clearAll()
}