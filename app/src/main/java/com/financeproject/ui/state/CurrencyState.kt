package com.financeproject.ui.state

import com.financeproject.data.api.Currency

sealed class CurrencyState {
    object Loading: CurrencyState()
    data class Success(
        val usd: Currency,
        val eur: Currency
    ): CurrencyState()
    data class Error(val message:String = "Error"): CurrencyState()
}
