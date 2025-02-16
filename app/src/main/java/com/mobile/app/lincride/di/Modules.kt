package com.mobile.app.lincride.di

import android.app.Application
import androidx.room.Room
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.mobile.app.lincride.BuildConfig
import com.mobile.app.lincride.db.LincDatabase
import com.mobile.app.lincride.db.dao.DriverDao
import com.mobile.app.lincride.db.dao.EstimateDao
import com.mobile.app.lincride.db.dao.HistoryDao
import com.mobile.app.lincride.home.PlacesManager
import com.mobile.app.lincride.repository.history.HistoryRepo
import com.mobile.app.lincride.repository.history.HistoryRepoImpl
import com.mobile.app.lincride.repository.rideRequest.RequestRideImpl
import com.mobile.app.lincride.repository.rideRequest.RequestRideManager
import com.mobile.app.lincride.repository.rideRequest.RequestRideRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object Modules {

    @Provides
    @Singleton
    fun providePlacesManager(client: PlacesClient): PlacesManager {
        return PlacesManager(client)
    }

    @Provides
    @Singleton
    fun providePlacesClient(app: Application): PlacesClient{
        val key = BuildConfig.MAPS_API_KEY
        Places.initialize(app, key)
        return Places.createClient(app)
    }

    @Provides
    @Singleton
    fun provideRequestRideManager() = RequestRideManager()

    @Provides
    @Singleton
    fun provideDatabase(app: Application): LincDatabase{
        return Room.databaseBuilder(app.applicationContext,
            LincDatabase::class.java, "linc_database")
            .build()

    }

    @Provides
    @Singleton
    fun provideDriverDao(database: LincDatabase) = database.driverDao()

    @Provides
    @Singleton
    fun provideEstimateDao(database: LincDatabase) = database.estimateDao()

    @Provides
    @Singleton
    fun provideHistoryDao(database: LincDatabase) = database.historyDao()

    @Provides
    @Singleton
    fun provideRideRepo(driverDao: DriverDao, estimateDao: EstimateDao): RequestRideRepo {
        return RequestRideImpl(driverDao, estimateDao)
    }

    @Provides
    @Singleton
    fun provideHistoryRepo(historyDao: HistoryDao, driverDao: DriverDao, estimateDao: EstimateDao): HistoryRepo {
        return HistoryRepoImpl(historyDao, driverDao, estimateDao)
    }
}
