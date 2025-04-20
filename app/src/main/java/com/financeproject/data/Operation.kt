package com.financeproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "operations",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category"]
        )
    ])
data class Operation(
    @PrimaryKey(autoGenerate = true)val id: Long,
    @ColumnInfo(name = "category")val category: Category,
    @ColumnInfo(name = "value")val value: Double,
    @ColumnInfo(name = "is_profit")val isprofit: Boolean,
    @ColumnInfo(name = "date")val date: String
)