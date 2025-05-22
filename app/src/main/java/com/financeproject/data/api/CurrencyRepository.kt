package com.financeproject.data.api

import com.financeproject.data.db.CurrencyDao

class CurrencyRepository(private val apiService: CurrencyApi = CurrencyApi.RetrofitClient.instance,
        private val currencyDao: CurrencyDao, var isCached: Boolean = false) {

    suspend fun getDailyRates(): CurrencyResponse {
        return apiService.getDailyRates()
    }

    suspend fun getCurrency(forced: Boolean = false): List<Currency> {
        val cache = currencyDao.getByCharCodes(listOf("USD","EUR"))
        var currency: MutableList<Currency> = emptyList<Currency>().toMutableList()
        if (forced || cache.isEmpty()) {
            try {
                val response = apiService.getDailyRates()
                val save = response.Valute.values.map { cur ->
                    cur.toEntity()
                }
                currencyDao.clearAll()
                currencyDao.insertAll(save)
                currency += currencyDao.getCurrency("USD").toCurrency()
                currency += currencyDao.getCurrency("EUR").toCurrency()
                isCached = false
            } catch (e: Exception){
                if (cache.isNotEmpty()){
                    for (cur in cache){
                        currency += cur.toCurrency()
                    }
                    isCached = true
                } else throw e
            }
        } else {
            for (cur in cache){
                currency += cur.toCurrency()
            }
            isCached = true
        }
        return currency
    }
}