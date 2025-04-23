package com.financeproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.financeproject.data.FinanceDataBase
import com.financeproject.data.Operation
import com.financeproject.data.OperationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    val allOperations: StateFlow<List<Operation>>

    init{
        val operationDao = FinanceDataBase.getDatabase(application).getOperationDao()
        operationRepository = OperationRepository(operationDao)
        allOperations = operationRepository.allOperations.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }
}