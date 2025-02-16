package com.mobile.app.lincride.repository.rideRequest

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.models.response.EstimateResponse
import kotlin.math.roundToInt
import kotlin.random.Random

class RequestRideManager {

    val isSurge = thereIsSurge()
    val isTraffic = thereIsTraffic()

    fun computeAmount(item: Pair<CustomPlacesModel, CustomPlacesModel>,
                      estimateResponse: EstimateResponse): Double{
        val source = item.first
        val destination = item.second
        val distance = computeDistance(source, destination)

        return calculateAmount(distance, estimateResponse, isSurge, isTraffic)
    }

    fun calculateAmount(distance: Double, estimateResponse: EstimateResponse,
                        surge: Boolean, traffic: Boolean): Double{
        val baseFare = estimateResponse.baseFare ?: 1.0
        val perKM = estimateResponse.distanceFare ?: 1.0
        val multiplier = estimateResponse.demandMultiplier ?: 1.0
        val trafficMultiplier = estimateResponse.trafficMultiplier ?: 1.0

        var amount = (distance * perKM) + baseFare

        if(surge) amount *= multiplier

        if(traffic) amount *= trafficMultiplier

        return amount
    }

    private fun computeDistance(source: CustomPlacesModel, dest: CustomPlacesModel): Double{
        val origin = LatLng(source.latitude, source.longitude)
        val destination = LatLng(dest.latitude, dest.longitude)
        return calculateDistance(origin, destination)
    }

    fun computeDistance(item: Pair<CustomPlacesModel, CustomPlacesModel>): String{
        val origin = LatLng(item.first.latitude, item.first.longitude)
        val destination = LatLng(item.second.latitude, item.second.longitude)
        val distance = calculateDistance(origin, destination).roundToInt()
        return  "~ $distance KM"
    }

    private fun calculateDistance(origin: LatLng, dest: LatLng): Double {
        return (SphericalUtil.computeDistanceBetween(origin, dest) / 1000)
    }

    private fun thereIsSurge() = Random.nextBoolean()

    private fun thereIsTraffic() = Random.nextBoolean()
}
