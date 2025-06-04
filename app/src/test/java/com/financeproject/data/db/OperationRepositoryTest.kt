package com.financeproject.data.db

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class OperationRepositoryTest {
    private lateinit var repository: OperationRepository
    private val operationDao: OperationDao = mock()

    @Before
    fun setup() {
        repository = OperationRepository(operationDao)
    }

    @Test
    fun `test get all operations`() = runTest {
        // Given
        val operations = listOf(
            createTestOperation(1, "Test 1", 100.0, true),
            createTestOperation(2, "Test 2", -50.0, false)
        )
        whenever(operationDao.getAllOperations()).thenReturn(flowOf(operations))
        
        // Create new repository instance after setting up the mock
        repository = OperationRepository(operationDao)

        // When
        val result = repository.allOperations.first()

        // Then
        assertThat(result).isEqualTo(operations)
    }

    @Test
    fun `test get all profit operations`() = runTest {
        // Given
        val profitOperations = listOf(
            createTestOperation(1, "Salary", 1000.0, true),
            createTestOperation(2, "Bonus", 500.0, true)
        )
        whenever(operationDao.getAllProfit()).thenReturn(flowOf(profitOperations))
        
        // Create new repository instance after setting up the mock
        repository = OperationRepository(operationDao)

        // When
        val result = repository.allProfit.first()

        // Then
        assertThat(result).isEqualTo(profitOperations)
        assertThat(result.all { it.isprofit }).isTrue()
    }

    @Test
    fun `test get all loss operations`() = runTest {
        // Given
        val lossOperations = listOf(
            createTestOperation(1, "Rent", 1000.0, false),
            createTestOperation(2, "Groceries", 200.0, false)
        )
        whenever(operationDao.getAllLoss()).thenReturn(flowOf(lossOperations))
        
        // Create new repository instance after setting up the mock
        repository = OperationRepository(operationDao)

        // When
        val result = repository.allLoss.first()

        // Then
        assertThat(result).isEqualTo(lossOperations)
        assertThat(result.all { !it.isprofit }).isTrue()
    }

    @Test
    fun `test insert operation`() = runTest {
        // Given
        val operation = createTestOperation(1, "Test", 100.0, true)

        // When
        repository.insertOperation(operation)

        // Then
        verify(operationDao).insertOperation(operation)
    }

    @Test
    fun `test update operation`() = runTest {
        // Given
        val operation = createTestOperation(1, "Test Updated", 150.0, true)

        // When
        repository.updateOperation(operation)

        // Then
        verify(operationDao).updateOperation(operation)
    }

    @Test
    fun `test delete operation`() = runTest {
        // Given
        val operation = createTestOperation(1, "Test", 100.0, true)

        // When
        repository.deleteOperation(operation)

        // Then
        verify(operationDao).deleteOperation(operation)
    }

    @Test
    fun `test reset data`() = runTest {
        // When
        repository.resetData()

        // Then
        verify(operationDao).resetData()
    }

    private fun createTestOperation(
        id: Long,
        description: String,
        value: Double,
        isProfit: Boolean
    ) = Operation(
        id = id,
        description = description,
        value = value,
        isprofit = isProfit,
        date = "2024-03-20"
    )
} 