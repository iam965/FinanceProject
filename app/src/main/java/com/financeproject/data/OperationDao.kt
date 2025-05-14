package com.financeproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OperationDao {
    @Query("SELECT * FROM operations")
    fun getAllOperations(): Flow<List<Operation>>

    @Query("SELECT * FROM operations WHERE is_profit = 1")
    fun getAllProfit(): Flow<List<Operation>>

    @Query("SELECT * FROM operations WHERE is_profit = 0")
    fun getAllLoss(): Flow<List<Operation>>

    @Insert
    fun insertOperation(vararg operation: Operation)

    @Update
    fun updateOperation(operation: Operation)

    @Delete
    fun deleteOperation(operation: Operation)

    @Query("DELETE FROM operations")
    fun resetData()
}