package com.mobile.app.lincride.models.request

import com.google.gson.annotations.SerializedName

data class RideRequest(
    @SerializedName("request_id")
    val requestId: String?
)
