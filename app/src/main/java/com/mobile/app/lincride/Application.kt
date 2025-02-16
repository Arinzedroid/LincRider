package com.mobile.app.lincride

import android.app.Application
import com.mobile.app.lincride.repository.rideRequest.RequestRideRepo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class LincRideApplication: Application() {

    @Inject lateinit var repo: RequestRideRepo

    override fun onCreate() {
        super.onCreate()

        repo.insertDefaultData()
    }
}
