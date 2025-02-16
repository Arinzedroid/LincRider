package com.mobile.app.lincride

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mobile.app.lincride.db.LincDatabase
import com.mobile.app.lincride.db.dao.DriverDao
import com.mobile.app.lincride.db.dao.EstimateDao
import com.mobile.app.lincride.db.dao.HistoryDao
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.models.HistoryEntity
import com.mobile.app.lincride.models.request.RideRequest
import com.mobile.app.lincride.repository.history.HistoryRepo
import com.mobile.app.lincride.repository.history.HistoryRepoImpl
import com.mobile.app.lincride.repository.rideRequest.RequestRideImpl
import com.mobile.app.lincride.repository.rideRequest.RequestRideManager
import com.mobile.app.lincride.repository.rideRequest.RequestRideRepo
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.collectLatest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.verify
import java.util.Date


class LincInstrumentationTest {

    private lateinit var database: LincDatabase
    private lateinit var estimateDao: EstimateDao
    private lateinit var driverDao: DriverDao
    private lateinit var historyDao: HistoryDao
    private lateinit var manager: RequestRideManager
    private lateinit var rideRepo: RequestRideRepo
    private lateinit var historyRepo: HistoryRepo

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LincDatabase::class.java).allowMainThreadQueries().build()

        estimateDao = database.estimateDao()
        driverDao = database.driverDao()
        historyDao = database.historyDao()
        manager = RequestRideManager()
        rideRepo = RequestRideImpl(driverDao, estimateDao)
        historyRepo = HistoryRepoImpl(historyDao, driverDao, estimateDao)
        rideRepo.insertDefaultData()
    }

    @After
    fun shutDown(){
        database.close()
    }

    @Test
    fun basic_fare_calculation() = runTest{
        val location = CustomPlacesModel("",0.0, 0.0)
        val estimate = rideRepo.fareEstimate(location to location)
        val distance = 5.0
        estimate.fold(
            onSuccess = { data ->
                assertThat(data).isNotNull()
                assertThat(data.baseFare).isNotNull()
                assertThat(data.demandMultiplier).isNotNull()
                assertThat(data.distanceFare).isNotNull()

                val amount = manager.calculateAmount(distance = distance, estimateResponse =  data,
                    surge = false, traffic = false)

                assertThat(amount).isEqualTo(7.5)
            },
            onFailure = { error ->
                assertThat(error).isNull()
            }
        )
    }

    @Test
    fun surge_pricing() = runTest{
        val location = CustomPlacesModel("",0.0, 0.0)
        val estimate = rideRepo.fareEstimate(location to location)
        val distance = 8.0
        estimate.fold(
            onSuccess = { data ->
                assertThat(data).isNotNull()
                assertThat(data.baseFare).isNotNull()
                assertThat(data.demandMultiplier).isNotNull()
                assertThat(data.distanceFare).isNotNull()

                //formula = (baseFare + Distance) * multiplier
                //surge for 8km = (2.5 + 8) * 1.5 = 15.75
                val amount = manager.calculateAmount(distance = distance, estimateResponse =  data,
                    surge = true, traffic = false)

                assertThat(amount).isEqualTo(15.75)
            },
            onFailure = {error ->
                assertThat(error).isNull()
            }
        )
    }

    @Test
    fun traffic_pricing() = runTest{
        val location = CustomPlacesModel("",0.0, 0.0)
        val estimate = rideRepo.fareEstimate(location to location)
        val distance = 6.0
        estimate.fold(
            onSuccess = { data ->
                assertThat(data).isNotNull()
                assertThat(data.baseFare).isNotNull()
                assertThat(data.demandMultiplier).isNotNull()
                assertThat(data.distanceFare).isNotNull()

                //formula = (baseFare + Distance) * multiplier
                //surge for 6km = (2.5 + 6) * 1.3 = 11.05
                val amount = manager.calculateAmount(distance = distance, estimateResponse =  data,
                    surge = false, traffic = true)

                assertThat(amount).isEqualTo(11.05)
            },
            onFailure = {error ->
                assertThat(error).isNull()
            }
        )
    }

    @Test
    fun store_ride_history_to_db() = runTest {
        val requestId = estimateDao.getAllEstimates().first().primaryKey
        val driverId = driverDao.getAllDrivers().first().primaryKey
        val history = HistoryEntity(date = Date().toString(), driverId = driverId,
            estimateId = requestId)

        historyRepo.addHistory(history)

        val result = historyRepo.getAllHistories()
        assertThat(result).isNotEmpty()
        result.forEach{ data ->
            assertThat(data.driver).isNotNull()
        }
    }

    @Test
    fun ride_request_confirmation() = runTest {
        val request = RideRequest("")
        val result = rideRepo.requestRide(request)

        result.fold(
            onSuccess = {data ->
                assertThat(data.driver).isNotNull()
                assertThat(data.status).isNotNull()
                assertThat(data.status?.lowercase()).isEqualTo("confirmed")
            },
            onFailure = {error ->
                assertThat(error).isNull()
            }
        )
    }
}
