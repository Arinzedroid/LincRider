package com.mobile.app.lincride.home

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.mobile.app.lincride.models.CustomPlacesModel

class PlacesManager(private val placesClient: PlacesClient) {

    fun getPredictions(query: String): List<CustomPlacesModel> {
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setCountries("NG")
            .build()

        val task = placesClient.findAutocompletePredictions(request)
        return try {
            val response = Tasks.await(task) ?: return emptyList()
            val predictions = response.autocompletePredictions

            predictions.mapNotNull { prediction ->
                val place = getPlaceDetails(prediction.placeId) ?: return@mapNotNull null
                CustomPlacesModel(
                    address = prediction.getPrimaryText(null).toString(),
                    latitude = place.location?.latitude ?: 0.0, longitude = place.location?.longitude ?: 0.0
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            emptyList()
        }
    }


    private fun getPlaceDetails(placeId: String): Place? {
        val request = FetchPlaceRequest.builder(placeId, listOf(Place.Field.LOCATION)).build()
        val task: Task<FetchPlaceResponse> = placesClient.fetchPlace(request)

        return try {
            val result = Tasks.await(task)
            result?.place
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}
