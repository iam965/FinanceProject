package com.financeproject.data.db

import kotlinx.coroutines.flow.Flow

class OperationRepository(private val operationDao: OperationDao) {
    val allOperations: Flow<List<Operation>> = operationDao.getAllOperations()
    val allProfit: Flow<List<Operation>> = operationDao.getAllProfit()
    val allLoss: Flow<List<Operation>> = operationDao.getAllLoss()

    suspend fun insertOperation(operation: Operation) {
        operationDao.insertOperation(operation)
    }

    suspend fun updateOperation(operation: Operation) {
        operationDao.updateOperation(operation)
    }

    suspend fun deleteOperation(operation: Operation) {
        operationDao.deleteOperation(operation)
    }

    suspend fun resetData() {
        operationDao.resetData()
    }
}