package com.mobile.app.lincride.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val driverId: Int,
    val estimateId: Int,
    val paymentMethod: Int = 1
){
    @Ignore
    var driver: DriverEntity? = null
    @Ignore
    var estimates: EstimateEntity? = null
}
