package com.financeproject.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operations")
data class Operation(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "value") val value: Double,
    @ColumnInfo(name = "is_profit") val isprofit: Boolean,
    @ColumnInfo(name = "date") val date: String
)