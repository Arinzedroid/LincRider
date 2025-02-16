package com.mobile.app.lincride.repository.rideRequest

import com.mobile.app.lincride.db.dao.DriverDao
import com.mobile.app.lincride.db.dao.EstimateDao
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.models.DriverEntity
import com.mobile.app.lincride.models.EstimateEntity
import com.mobile.app.lincride.models.response.EstimateResponse
import com.mobile.app.lincride.models.request.RideRequest
import com.mobile.app.lincride.models.response.Driver
import com.mobile.app.lincride.models.response.RideResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface RequestRideRepo {
    fun insertDefaultData()
    suspend fun fareEstimate(input: Pair<CustomPlacesModel, CustomPlacesModel>): Result<EstimateResponse>
    suspend fun requestRide(request: RideRequest): Result<RideResponse>
}

class RequestRideImpl @Inject constructor(private val driverDao: DriverDao,
                                          private val estimateDao: EstimateDao): RequestRideRepo {


    override fun insertDefaultData() = insertAllData()

    private fun insertAllData(){
        CoroutineScope(Dispatchers.IO).launch{
            val driver = DriverEntity(
                name = "John Doe",
                car = "Toyota Prius",
                plateNumber = "XYZ1234"
            )
            val estimate = EstimateEntity(
                baseFare = 2.5,
                perKM = 1.0,
                surgeMultiplier = 1.5,
                trafficMultiplier = 1.3
            )
            estimateDao.insertEstimate(estimate)
            driverDao.insertDriver(driver)
        }
    }

    override suspend fun fareEstimate(input: Pair<CustomPlacesModel, CustomPlacesModel>): Result<EstimateResponse> =
        withContext(Dispatchers.IO){
        return@withContext try{
            val estimate = estimateDao.getAllEstimates().first()
            val response = EstimateResponse(baseFare = estimate.baseFare,
                demandMultiplier = estimate.surgeMultiplier,
                trafficMultiplier = estimate.trafficMultiplier, distanceFare = estimate.perKM,
                totalFare = null, requestId = estimate.primaryKey)
            Result.success(response)
        }catch (ex: Exception){
            Result.failure(ex)
        }
    }

    override suspend fun requestRide(request: RideRequest): Result<RideResponse> = withContext(Dispatchers.IO){
        return@withContext try{
            val driver = driverDao.getAllDrivers().first()
            val response = RideResponse(driver =
            Driver(driverId = driver.primaryKey, car = driver.car, name = driver.name,
                plateNumber = driver.plateNumber), status = "confirmed",
                requestId = driver.primaryKey.toString(), estimatedArrival = "5 min")
            Result.success(response)
        }catch (ex: Exception){
            Result.failure(ex)
        }
    }


}
