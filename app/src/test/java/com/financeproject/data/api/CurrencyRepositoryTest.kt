package com.financeproject.data.api

import com.financeproject.data.db.CurrencyDao
import com.financeproject.data.db.CurrencyEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class CurrencyRepositoryTest {
    private lateinit var repository: CurrencyRepository
    private val apiService: CurrencyApi = mock()
    private val currencyDao: CurrencyDao = mock()

    @Before
    fun setup() {
        repository = CurrencyRepository(apiService, currencyDao)
    }

    @Test
    fun `test get currency from API when cache is empty`() = runTest {
        // Given
        val usdCurrency = createTestCurrency("USD", 75.0)
        val eurCurrency = createTestCurrency("EUR", 85.0)
        val response = CurrencyResponse(
            Date = "2024-03-20",
            PreviousDate = "2024-03-19",
            PreviousURL = "http://example.com",
            Timestamp = "2024-03-20T12:00:00Z",
            Valute = mapOf(
                "USD" to usdCurrency,
                "EUR" to eurCurrency
            )
        )
        whenever(currencyDao.getByCharCodes(any())).thenReturn(emptyList())
        whenever(apiService.getDailyRates()).thenReturn(response)
        whenever(currencyDao.getCurrency("USD")).thenReturn(usdCurrency.toEntity())
        whenever(currencyDao.getCurrency("EUR")).thenReturn(eurCurrency.toEntity())

        // When
        val result = repository.getCurrency(forced = false)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.CharCode }).containsExactly("USD", "EUR")
        verify(currencyDao).clearAll()
        verify(currencyDao).insertAll(any())
        assertThat(repository.isCached).isFalse()
    }

    @Test
    fun `test get currency from cache when available`() = runTest {
        // Given
        val usdEntity = createTestCurrency("USD", 75.0).toEntity()
        val eurEntity = createTestCurrency("EUR", 85.0).toEntity()
        whenever(currencyDao.getByCharCodes(any())).thenReturn(listOf(usdEntity, eurEntity))

        // When
        val result = repository.getCurrency(forced = false)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.CharCode }).containsExactly("USD", "EUR")
        assertThat(repository.isCached).isTrue()
    }

    @Test
    fun `test force refresh currency from API`() = runTest {
        // Given
        val usdCurrency = createTestCurrency("USD", 75.0)
        val eurCurrency = createTestCurrency("EUR", 85.0)
        val response = CurrencyResponse(
            Date = "2024-03-20",
            PreviousDate = "2024-03-19",
            PreviousURL = "http://example.com",
            Timestamp = "2024-03-20T12:00:00Z",
            Valute = mapOf(
                "USD" to usdCurrency,
                "EUR" to eurCurrency
            )
        )
        whenever(apiService.getDailyRates()).thenReturn(response)
        whenever(currencyDao.getCurrency("USD")).thenReturn(usdCurrency.toEntity())
        whenever(currencyDao.getCurrency("EUR")).thenReturn(eurCurrency.toEntity())

        // When
        val result = repository.getCurrency(forced = true)

        // Then
        assertThat(result).hasSize(2)
        verify(currencyDao).clearAll()
        verify(currencyDao).insertAll(any())
        assertThat(repository.isCached).isFalse()
    }

    @Test
    fun `test fallback to cache when API fails`() = runTest {
        // Given
        val usdEntity = createTestCurrency("USD", 75.0).toEntity()
        val eurEntity = createTestCurrency("EUR", 85.0).toEntity()
        whenever(currencyDao.getByCharCodes(any())).thenReturn(listOf(usdEntity, eurEntity))
        whenever(apiService.getDailyRates()).thenThrow(RuntimeException("Network error"))

        // When
        val result = repository.getCurrency(forced = true)

        // Then
        assertThat(result).hasSize(2)
        assertThat(result.map { it.CharCode }).containsExactly("USD", "EUR")
        assertThat(repository.isCached).isTrue()
    }

    private fun createTestCurrency(
        charCode: String,
        value: Double
    ) = Currency(
        ID = "R0$charCode",
        NumCode = "000",
        CharCode = charCode,
        Nominal = 1,
        Name = "$charCode Currency",
        Value = value,
        Previous = value
    )
} 