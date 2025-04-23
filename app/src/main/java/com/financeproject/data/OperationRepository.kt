package com.financeproject.data

import kotlinx.coroutines.flow.Flow

class OperationRepository(private val operationDao: OperationDao) {
    val allProfit: Flow<List<Operation>> = operationDao.getAllProfit()
    val allLoss: Flow<List<Operation>> = operationDao.getAllLoss()

    suspend fun insertOperation(operation: Operation){
        operationDao.insertOperation(operation)
    }

    suspend fun updateOperation(operation: Operation){
        operationDao.updateOperation(operation)
    }

    suspend fun deleteOperation(operation: Operation){
        operationDao.deleteOperation(operation)
    }
}