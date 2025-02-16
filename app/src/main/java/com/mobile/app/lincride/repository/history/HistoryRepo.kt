package com.mobile.app.lincride.repository.history

import com.mobile.app.lincride.db.dao.DriverDao
import com.mobile.app.lincride.db.dao.EstimateDao
import com.mobile.app.lincride.db.dao.HistoryDao
import com.mobile.app.lincride.models.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface HistoryRepo {
    suspend fun getAllHistories(): List<HistoryEntity>
    suspend fun addHistory(history: HistoryEntity)
}

class HistoryRepoImpl @Inject constructor(private val historyDao: HistoryDao,
                                          private val driverDao: DriverDao,
                                          private val estimateDao: EstimateDao): HistoryRepo{

    override suspend fun getAllHistories(): List<HistoryEntity> {
        return historyDao.getAllHistories()
            .onEach { history ->
                history.driver = driverDao.getDriverById(history.driverId)
                history.estimates = estimateDao.getEstimate(history.estimateId)  }
    }

    override suspend fun addHistory(history: HistoryEntity) = withContext(Dispatchers.IO){
        historyDao.insertHistory(history)
    }

}
