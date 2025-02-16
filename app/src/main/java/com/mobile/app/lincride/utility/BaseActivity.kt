package com.mobile.app.lincride.utility

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mobile.app.lincride.models.CustomPlacesModel
import java.util.Locale

abstract class BaseActivity: AppCompatActivity() {
    private lateinit var locationProvider: FusedLocationProviderClient
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted && coarseLocationGranted) {
                getLastKnownLocation()
            }else {
                listener?.onPermissionDenied()
            }
        }

    abstract val listener: LocationListener?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationProvider = LocationServices.getFusedLocationProviderClient(this)
    }

    protected fun checkLocationPermission(){
        if(Utils.hasPermission(this)){
            getLastKnownLocation()
        }else{
            requestLocation()
        }
    }

    private fun requestLocation(){
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        locationPermissionLauncher.launch(permissions.toTypedArray())
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(){
        locationProvider.lastLocation.addOnSuccessListener { data ->
            if(data != null){
                val location = CustomPlacesModel(latitude = data.latitude,
                    longitude = data.longitude, address = "")
                listener?.onUserLocation(location)
                getAddressFromLatLng(location)
            }
        }
    }

    private fun getAddressFromLatLng(location: CustomPlacesModel) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses?.filterNotNull().orEmpty().isNotEmpty()) {
                val address = addresses!![0]
                val addressText = StringBuilder()

                for (i in 0..address.maxAddressLineIndex) {
                    addressText.append(address.getAddressLine(i)).append("\n")
                }
                listener?.onUserAddress(location.copy(address =  addressText.toString()))
            } else {
                //address not found
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}

interface LocationListener{
    fun onUserLocation(location: CustomPlacesModel)
    fun onPermissionDenied()
    fun onUserAddress(location: CustomPlacesModel)
}
