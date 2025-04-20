package com.financeproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): List<Category>

    @Query("SELECT * FROM category WHERE is_profit = 1")
    fun getAllProfitCategories(): List<Operation>

    @Query("SELECT * FROM category WHERE is_profit = 0")
    fun getAllLossCategories(): List<Operation>

    @Insert
    fun insertCategory(vararg category: Category)

    @Delete
    fun deleteCategory(category: Category)
}