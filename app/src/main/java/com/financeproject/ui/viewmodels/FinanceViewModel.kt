package com.financeproject.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.financeproject.data.FinanceDataBase
import com.financeproject.data.Operation
import com.financeproject.data.OperationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class FinanceViewModel(application: Application): AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    val allProfit: StateFlow<List<Operation>>
    val allLoss: StateFlow<List<Operation>>

    init{
        val operationDao = FinanceDataBase.getDatabase(application).getOperationDao()
        operationRepository = OperationRepository(operationDao)
        allProfit = operationRepository.allProfit.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        allLoss = operationRepository.allLoss.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

/*    fun changeTheme(){
        try {
            uiState.value.isDarkTheme = !uiState.value.isDarkTheme
            uiState.value.changeTheme()
        } catch (e: Exception) {
        }
    }*/

    class FinanceViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FinanceViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}