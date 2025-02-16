package com.mobile.app.lincride.requestRide

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.models.Fourthly
import com.mobile.app.lincride.models.response.EstimateResponse
import com.mobile.app.lincride.repository.rideRequest.RequestRideManager
import com.mobile.app.lincride.repository.rideRequest.RequestRideRepo
import com.mobile.app.lincride.utility.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmRideViewModel @Inject constructor(private val repo: RequestRideRepo,
                                               private val manager: RequestRideManager
): ViewModel() {

    private val _estimate = MutableLiveData<Fourthly<String, String, Boolean, Boolean>>()
    val estimate = _estimate

    private val _error = MutableLiveData<String>()
    val error = _error

    var requestId: Int? = null

    private fun getEstimate(input: Pair<CustomPlacesModel, CustomPlacesModel>){
        viewModelScope.launch {
            repo.fareEstimate(input).fold(
                onSuccess = { requestId = it.requestId
                    calculateFare(input, it) },
                onFailure = { it.printStackTrace()
                    _error.value = it.message
                }
            )
        }
    }


    fun getFareAmount(input: Pair<CustomPlacesModel, CustomPlacesModel>){
        getEstimate(input)
    }

    private fun calculateFare(input: Pair<CustomPlacesModel, CustomPlacesModel>,
                              estimateResponse: EstimateResponse){
        val amount = Utils.formatCurrency(manager.computeAmount(input, estimateResponse))
        val distance = manager.computeDistance(input)
        _estimate.value = Fourthly(first = amount, second = distance,
            third = manager.isSurge, fourth = manager.isTraffic)
    }
}
