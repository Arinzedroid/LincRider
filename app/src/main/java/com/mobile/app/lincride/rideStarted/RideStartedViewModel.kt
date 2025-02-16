package com.mobile.app.lincride.rideStarted

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.app.lincride.models.request.RideRequest
import com.mobile.app.lincride.models.response.RideResponse
import com.mobile.app.lincride.repository.rideRequest.RequestRideRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RideStartedViewModel @Inject constructor(private val repo: RequestRideRepo): ViewModel() {

    private val _response = MutableLiveData<RideResponse>()
    val response = _response

    private val _error = MutableLiveData<String>()
    val error = _error

    fun requestRide(request: RideRequest){
        viewModelScope.launch {
            repo.requestRide(request).fold(
                onSuccess = {_response.value = it},
                onFailure = { it.printStackTrace()
                    _error.value = it.message
                }
            )
        }
    }
}
