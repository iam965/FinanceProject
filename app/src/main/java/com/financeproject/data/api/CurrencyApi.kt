package com.financeproject.data.api

import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface CurrencyApi {
    @GET("daily_json.js")
    suspend fun getDailyRates(): CurrencyResponse

    object RetrofitClient {
        private const val BASE_URL = "https://www.cbr-xml-daily.ru/"

        val instance: CurrencyApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(CurrencyApi::class.java)
        }
    }
}