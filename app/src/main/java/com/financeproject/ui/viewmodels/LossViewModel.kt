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

class LossViewModel(application: Application): AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    private val categoryRepository: CategoryRepository
    val allLoss: StateFlow<List<Operation>>
    val allLossCategory: StateFlow<List<Category>>

    init {
        val operationDao = FinanceDataBase.getDatabase(application).getOperationDao()
        val categoryDao = FinanceDataBase.getDatabase(application).getCategoryDao()
        operationRepository = OperationRepository(operationDao)
        allLoss = operationRepository.allLoss.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        categoryRepository = CategoryRepository(categoryDao)
        allLossCategory = categoryRepository.allLossCategory.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    fun addLoss(loss: Operation){
        viewModelScope.launch{
            operationRepository.insertOperation(loss)
        }
    }

    fun addLossCategory(lossCategory: Category){
        viewModelScope.launch {
            categoryRepository.insertCategory(lossCategory)
        }
    }

    fun updateLoss(loss: Operation){
        viewModelScope.launch {
            operationRepository.updateOperation(loss)
        }
    }

    fun updateLossCategory(lossCategory: Category){
        viewModelScope.launch {
            categoryRepository.updateCategory(lossCategory)
        }
    }

    fun deleteLoss(loss: Operation){
        viewModelScope.launch {
            operationRepository.deleteOperation(loss)
        }
    }

    fun deleteLossCategory(lossCategory: Category){
        viewModelScope.launch {
            categoryRepository.deleteCategory(lossCategory)
        }
    }
}