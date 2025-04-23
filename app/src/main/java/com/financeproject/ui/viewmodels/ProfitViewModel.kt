package com.financeproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.financeproject.data.Category
import com.financeproject.data.CategoryRepository
import com.financeproject.data.FinanceDataBase
import com.financeproject.data.Operation
import com.financeproject.data.OperationDao
import com.financeproject.data.OperationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfitViewModel(application: Application): AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    private val categoryRepository: CategoryRepository
    val allProfit: StateFlow<List<Operation>>
    val allProfitCategory: StateFlow<List<Category>>

    init {
        val operationDao = FinanceDataBase.getDatabase(application).getOperationDao()
        val categoryDao = FinanceDataBase.getDatabase(application).getCategoryDao()
        operationRepository = OperationRepository(operationDao)
        allProfit = operationRepository.allProfit.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        categoryRepository = CategoryRepository(categoryDao)
        allProfitCategory = categoryRepository.allProfitCategory.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun addProfit(profit: Operation){
        viewModelScope.launch{
            operationRepository.insertOperation(profit)
        }
    }

    fun addProfitCategory(profitCategory: Category){
        viewModelScope.launch {
            categoryRepository.insertCategory(profitCategory)
        }
    }

    fun updateProfit(profit: Operation){
        viewModelScope.launch {
            operationRepository.updateOperation(profit)
        }
    }

    fun updateProfitCategory(profitCategory: Category){
        viewModelScope.launch {
            categoryRepository.updateCategory(profitCategory)
        }
    }

    fun deleteProfit(profit: Operation){
        viewModelScope.launch {
            operationRepository.deleteOperation(profit)
        }
    }

    fun deleteProfitCategory(profitCategory: Category){
        viewModelScope.launch {
            categoryRepository.deleteCategory(profitCategory)
        }
    }
}