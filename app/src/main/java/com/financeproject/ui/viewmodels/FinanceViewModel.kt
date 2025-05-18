package com.financeproject.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.financeproject.data.FinanceDataBase
import com.financeproject.data.Operation
import com.financeproject.data.OperationRepository
import com.financeproject.ui.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FinanceViewModel(application: Application, private val UiState: UIState): AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    val allProfit: StateFlow<List<Operation>>
    val allLoss: StateFlow<List<Operation>>
    val allOperations: StateFlow<List<Operation>>
    private val _isDarkTheme = mutableStateOf(UiState.isDarkTheme)
    val isDarkTheme: State<Boolean> = _isDarkTheme
    private val _valute: MutableState<String?> = mutableStateOf(UiState.selectedValute)

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
        allOperations = operationRepository.allOperations.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    }

    fun changeTheme(){
        _isDarkTheme.value = !_isDarkTheme.value
        UiState.changeTheme()
    }

    fun changeValute(valute: String){
        _valute.value = valute
        UiState.changeValute(valute)
    }

    fun getValute(): String{
        return _valute.value.toString()
    }

    fun resetData(){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.resetData()
        }
    }

    fun insertOperation(operation: Operation){
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.insertOperation(operation)
        }
    }

    fun deleteOperation(operation: Operation){
        viewModelScope.launch(Dispatchers.IO){
            operationRepository.deleteOperation(operation)
        }
    }

    class FinanceViewModelFactory(private val application: Application, UiState: UIState) : ViewModelProvider.Factory {
        private val uiState = UiState
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FinanceViewModel(application, uiState) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}