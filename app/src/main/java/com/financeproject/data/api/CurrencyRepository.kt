package com.financeproject.data.api

class CurrencyRepository {
    private val apiService = CurrencyApi.RetrofitClient.instance

    suspend fun getDailyRates(): CurrencyResponse {
        return apiService.getDailyRates()
    }
}