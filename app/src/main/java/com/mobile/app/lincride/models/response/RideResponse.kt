package com.mobile.app.lincride.models.response


import com.google.gson.annotations.SerializedName

data class RideResponse(
    @SerializedName("driver")
    val driver: Driver?,
    @SerializedName("estimated_arrival")
    val estimatedArrival: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("request_id")
    val requestId: String?,
)

data class Driver(
    @SerializedName("driver_id")
    val driverId: Int?,
    @SerializedName("car")
    val car: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("plate_number")
    val plateNumber: String?
)
