package com.mobile.app.lincride.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estimates_table")
data class EstimateEntity (
    @PrimaryKey(autoGenerate = true)
    val primaryKey: Int = 0,
    val surgeMultiplier: Double,
    val trafficMultiplier: Double,
    val baseFare: Double,
    val perKM: Double,
)
