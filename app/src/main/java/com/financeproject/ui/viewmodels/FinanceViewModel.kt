package com.financeproject.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.financeproject.data.api.CurrencyRepository
import com.financeproject.data.db.FinanceDataBase
import com.financeproject.data.db.Operation
import com.financeproject.data.db.OperationRepository
import com.financeproject.ui.state.CurrencyState
import com.financeproject.ui.state.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application, private val UiState: UIState) :
    AndroidViewModel(application) {
    private val operationRepository: OperationRepository
    val allProfit: StateFlow<List<Operation>>
    val allLoss: StateFlow<List<Operation>>
    val allOperations: StateFlow<List<Operation>>
    private val _isDarkTheme = mutableStateOf(UiState.isDarkTheme)
    val isDarkTheme: State<Boolean> = _isDarkTheme
    private val _valute: MutableState<String?> = mutableStateOf(UiState.selectedValute)
    private val _language: MutableState<String?> = mutableStateOf(UiState.selectedLanguage)
    private val currencyRepository: CurrencyRepository
    var currency = mutableStateOf<CurrencyState>(CurrencyState.Loading)

    init {
        val operationDao = FinanceDataBase.getDatabase(application).getOperationDao()
        val currencyDao = FinanceDataBase.getDatabase(application).getCurrencyDao()
        operationRepository = OperationRepository(operationDao)
        currencyRepository = CurrencyRepository(currencyDao = currencyDao)
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
        getDailyRates(true)
    }

    fun changeTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        UiState.changeTheme()
    }

    fun changeValute(valute: String) {
        _valute.value = valute
        UiState.changeValute(valute)
    }

    fun changeLanguage(language: String){
        _language.value =language
        UiState.changeLanguage(language)
    }

    fun getValute(): String {
        return _valute.value.toString()
    }

    fun getLanguage(): String{
        return _language.value.toString()
    }

    fun resetData() {
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.resetData()
        }
    }

    fun insertOperation(operation: Operation) {
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.insertOperation(operation)
        }
    }

    fun deleteOperation(operation: Operation) {
        viewModelScope.launch(Dispatchers.IO) {
            operationRepository.deleteOperation(operation)
        }
    }

    fun getDailyRates(forced: Boolean = false){
        currency.value = CurrencyState.Loading
        viewModelScope.launch {
            try {
                val currencies = currencyRepository.getCurrency(forced)
                val usd = currencies.find { it.CharCode == "USD" }
                val eur = currencies.find { it.CharCode == "EUR" }
                if (usd != null && eur != null){
                    currency.value = CurrencyState.Success(usd = usd, eur = eur)
                } else {
                    currency.value = CurrencyState.Error("Cant find currency")
                }
            } catch (e: Exception){
                currency.value = CurrencyState.Error(e.message.toString())
            }
        }
    }

    fun isCachedData(): Boolean{
        return currencyRepository.isCached
    }

    class FinanceViewModelFactory(private val application: Application, UiState: UIState) :
        ViewModelProvider.Factory {
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