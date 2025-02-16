package com.mobile.app.lincride.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomPlacesModel (
    val address: String,
    val latitude: Double,
    val longitude: Double
): Parcelable

