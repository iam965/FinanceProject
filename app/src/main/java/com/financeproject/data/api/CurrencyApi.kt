package com.financeproject.data.api

import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface CurrencyApi {
    @GET("daily_json.js")
    suspend fun getDailyRates(): CurrencyResponse

    object RetrofitClient {
        private const val BASE_URL = "https://www.cbr-xml-daily.ru/"

        val instance: CurrencyApi by lazy {
            val httpClient = OkHttpClient.Builder()
            httpClient.connectTimeout(15, TimeUnit.SECONDS)
            httpClient.readTimeout(15, TimeUnit.SECONDS)
            httpClient.writeTimeout(15, TimeUnit.SECONDS)
            val client = httpClient.build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            retrofit.create(CurrencyApi::class.java)
        }
    }
}