package com.mobile.app.lincride.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.app.lincride.db.dao.DriverDao
import com.mobile.app.lincride.db.dao.EstimateDao
import com.mobile.app.lincride.db.dao.HistoryDao
import com.mobile.app.lincride.models.DriverEntity
import com.mobile.app.lincride.models.EstimateEntity
import com.mobile.app.lincride.models.HistoryEntity

@Database(entities = [EstimateEntity::class, DriverEntity::class, HistoryEntity::class], version = 1, exportSchema = false)
abstract class LincDatabase: RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun estimateDao(): EstimateDao
    abstract fun historyDao(): HistoryDao
}
