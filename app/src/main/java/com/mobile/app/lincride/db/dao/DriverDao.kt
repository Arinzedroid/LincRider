package com.mobile.app.lincride.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobile.app.lincride.models.DriverEntity

@Dao
interface DriverDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriver(driver: DriverEntity)

    @Query("SELECT * FROM driver_table")
    suspend fun getAllDrivers(): List<DriverEntity>

    @Query("SELECT * FROM driver_table WHERE name = :name")
    suspend fun getDriver(name: String): DriverEntity

    @Query("SELECT * FROM driver_table WHERE primaryKey = :id")
    suspend fun getDriverById(id: Int): DriverEntity

    @Delete
    suspend fun deleteDriver(driver: DriverEntity)
}
