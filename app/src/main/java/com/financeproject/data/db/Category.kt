package com.financeproject.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class Category(
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "is_profit") val isprofit: Boolean
)