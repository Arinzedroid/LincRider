package com.mobile.app.lincride.home

interface DialogCallback {
    fun onCancelRide()
    fun onStartRide(requestId: Int?)
    fun onRideStarted(requestId: Int?, driverId: Int?)
}
