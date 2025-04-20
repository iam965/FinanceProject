package com.financeproject.data

import kotlinx.coroutines.flow.Flow

class OperationRepository(operationDao: OperationDao) {
    val allProfit: Flow<List<Operation>> = operationDao.getAllProfit()
    val allLoss: Flow<List<Operation>> = operationDao.getAllLoss()


}