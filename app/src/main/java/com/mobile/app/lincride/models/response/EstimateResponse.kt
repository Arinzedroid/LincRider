package com.mobile.app.lincride.models.response
import com.google.gson.annotations.SerializedName

data class EstimateResponse(
    @SerializedName("base_fare")
    val baseFare: Double?,
    @SerializedName("demand_multiplier")
    val demandMultiplier: Double?,
    @SerializedName("traffic_multiplier")
    val trafficMultiplier: Double?,
    @SerializedName("distance_fare")
    val distanceFare: Double?,
    @SerializedName("total_fare")
    val totalFare: Double?,
    @SerializedName("request_id")
    val requestId: Int?
)
