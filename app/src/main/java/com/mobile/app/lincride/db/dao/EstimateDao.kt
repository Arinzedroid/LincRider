package com.mobile.app.lincride.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.app.lincride.models.EstimateEntity

@Dao
interface EstimateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimate(estimate: EstimateEntity)

    @Query("SELECT * FROM estimates_table")
    suspend fun getAllEstimates(): List<EstimateEntity>

    @Query("SELECT * FROM estimates_table WHERE primaryKey = :id")
    suspend fun getEstimate(id: Int): EstimateEntity

    @Delete
    suspend fun deleteEstimate(estimate: EstimateEntity)
}
