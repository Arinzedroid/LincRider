package com.mobile.app.lincride.home

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.mobile.app.lincride.R
import com.mobile.app.lincride.databinding.ActivityMapsBinding
import com.mobile.app.lincride.models.CustomPlacesModel
import com.mobile.app.lincride.models.HistoryEntity
import com.mobile.app.lincride.requestRide.ConfirmRideFragment
import com.mobile.app.lincride.rideStarted.RideStartedDialog
import com.mobile.app.lincride.utility.openLocationSettings
import com.mobile.app.lincride.utility.BaseActivity
import com.mobile.app.lincride.utility.LocationListener
import com.mobile.app.lincride.utility.hideKeyboard
import com.mobile.app.lincride.utility.toast
import com.mobile.app.lincride.utility.toggleVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class MapsActivity : BaseActivity(), LocationListener, DialogCallback {

    override val listener: LocationListener = this
    @Inject lateinit var manager: PlacesManager

    private val viewModel: MapsViewModel by viewModels()
    private var mMap: GoogleMap? = null
    private lateinit var binding: ActivityMapsBinding
    private lateinit var userLocationAdapter: AutoCompleteAdapter
    private lateinit var userDestinationAdapter: AutoCompleteAdapter
    private var sourceLocation: CustomPlacesModel? = null
    private var destinationLocation: CustomPlacesModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED){
                mMap = mapFragment.awaitMap()
                checkLocationPermission()
            }
        }
        initView()
    }

    private fun initView(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        initSearch()
    }

    private fun initMap(location: CustomPlacesModel){
        try{
            val userLocation = LatLng(location.latitude, location.longitude)
            mMap?.addMarker{ position(userLocation) }
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 12f)
            mMap?.moveCamera(cameraUpdate)
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    private fun initSearch(){

        userLocationAdapter = AutoCompleteAdapter(this, manager)
        userDestinationAdapter = AutoCompleteAdapter(this, manager)

        binding.sourceAutoCompleteET.setAdapter(userLocationAdapter)
        binding.destinationAutoCompleteET.setAdapter(userDestinationAdapter)

        binding.sourceAutoCompleteET.setOnItemClickListener{_, _, position, _ ->
            val selected = userLocationAdapter.getItem(position)
            binding.sourceAutoCompleteET.setText(selected)
            binding.sourceAutoCompleteET.setSelection(selected.length)
            sourceLocation = userLocationAdapter.getPlaces(position)
            handleUserSelectedLocations()
        }

        binding.destinationAutoCompleteET.setOnItemClickListener{_, _, position, _ ->
            val selected = userDestinationAdapter.getItem(position)
            binding.destinationAutoCompleteET.setText(selected)
            binding.destinationAutoCompleteET.setSelection(selected.length)
            destinationLocation = userDestinationAdapter.getPlaces(position)
            handleUserSelectedLocations()
        }

    }

    private fun validate(): Boolean{
        return (sourceLocation != null && destinationLocation != null) &&
                (binding.sourceAutoCompleteET.text.toString().isNotEmpty() &&
                        binding.destinationAutoCompleteET.text.toString().isNotEmpty())
    }

    private fun clearInfo(){
        mMap?.clear()
        destinationLocation = null
        binding.destinationAutoCompleteET.clearListSelection()
        binding.destinationAutoCompleteET.setText("")
    }

    private fun refreshMap(){
        val sourceLatLng = LatLng(sourceLocation!!.latitude, sourceLocation!!.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(sourceLatLng, 12f)
        mMap?.moveCamera(cameraUpdate)
    }

    private fun createHistory(driverId: Int?, requestId: Int?){
        if(driverId != null && requestId != null){
            val historyEntity = HistoryEntity(driverId = driverId, estimateId = requestId,
                date = Date().toString())

            viewModel.createHistory(historyEntity)
        }
    }

    private fun handleUserSelectedLocations(){
        if(validate()){
            val sourceLatLng = LatLng(sourceLocation!!.latitude, sourceLocation!!.longitude)
            val destinationLatLng = LatLng(destinationLocation!!.latitude, destinationLocation!!.longitude)
            mMap?.clear()
            mMap?.addMarker { position(destinationLatLng) }
            val polyline = PolylineOptions()
                .add(sourceLatLng, destinationLatLng)
                .width(10f)
                .color(Color.BLUE)
                .geodesic(false)
            mMap?.addPolyline(polyline)

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(destinationLatLng, 12f)
            mMap?.moveCamera(cameraUpdate)
            showRideRequest()
        }
    }

    private fun simulateLoading(action: () -> Unit){
        lifecycleScope.launch {
            binding.root.hideKeyboard()
            binding.loadingView.toggleVisibility(true)
            delay(4.seconds)
            binding.loadingView.toggleVisibility(false)
            action()
        }
    }

    private fun showRideRequest(){
        if(sourceLocation != null && destinationLocation != null){
            simulateLoading {
                val dialog = ConfirmRideFragment.newInstance(source = sourceLocation!!,
                    destination = destinationLocation!!)
                dialog.show(supportFragmentManager, "ConfirmRideFragment")
            }
        }
    }

    override fun onUserLocation(location: CustomPlacesModel) {
        sourceLocation = location
        initMap(location)
    }

    override fun onPermissionDenied() {
        Toast.makeText(this, "You need to accept location permission to use this app",
            Toast.LENGTH_SHORT).show()

        openLocationSettings()
    }

    override fun onUserAddress(location: CustomPlacesModel) {
        binding.sourceAutoCompleteET.setText(location.address)
        binding.destinationAutoCompleteET.requestFocus()
        sourceLocation = location
    }

    override fun onCancelRide() {
        simulateLoading {
            clearInfo()
        }
    }

    override fun onStartRide(requestId: Int?) {
        if(requestId == null){
            toast("Cannot start ride. Invalid request ID")
            return
        }

        simulateLoading {
            refreshMap()
            val direction = destinationLocation?.address ?: ""
            val dialog = RideStartedDialog.newInstance(direction, requestId)
            dialog.show(supportFragmentManager, "RideStartedDialog")
        }
    }

    override fun onRideStarted(requestId: Int?, driverId: Int?) {
        createHistory(requestId = requestId, driverId = driverId)
    }
}
