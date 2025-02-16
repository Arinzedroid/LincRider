package com.mobile.app.lincride.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_table")
data class DriverEntity(
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val name: String,
    val car: String,
    val plateNumber: String
)
