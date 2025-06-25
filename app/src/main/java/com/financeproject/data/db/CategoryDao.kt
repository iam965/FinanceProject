package com.financeproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE is_profit = 1")
    fun getAllProfitCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE is_profit = 0")
    fun getAllLossCategories(): Flow<List<Category>>

    @Query("SELECT * FROM category WHERE id = :categoryId LIMIT 1")
    fun getCategoryById(categoryId: Int): Flow<Category?>

    @Insert
    fun insertCategory(vararg category: Category)

    @Update
    fun updateCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}