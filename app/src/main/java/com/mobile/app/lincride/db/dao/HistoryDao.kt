package com.mobile.app.lincride.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.app.lincride.models.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getAllHistories(): List<HistoryEntity>

    @Query("SELECT * FROM history_table WHERE id = :id")
    suspend fun getHistory(id: Int): HistoryEntity

    @Delete
    suspend fun deleteHistory(history: HistoryEntity)

}
