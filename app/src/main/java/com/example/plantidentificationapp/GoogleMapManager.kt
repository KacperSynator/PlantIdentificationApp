package com.example.plantidentificationapp

import android.content.Context
import android.util.Log
import com.example.plantidentificationapp.adapters.MapMarkerInfoWindowAdapter
import com.example.plantidentificationapp.classes.MapMarkerData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.net.URL

class GoogleMapManager(private val context: Context) : OnMapReadyCallback {
    private val logTag = "GoogleMapManager"

    private lateinit var googleMap: GoogleMap

    private var geoManager: GeoLocalizationManager = GeoLocalizationManager(context)


    override fun onMapReady(map: GoogleMap) {
        map.let {
            Log.i(logTag, "Google map ready")
            googleMap = it
            googleMap.setInfoWindowAdapter(MapMarkerInfoWindowAdapter(context))
            geoManager.getLocation { location ->
                if (location == null) {
                    Log.e(logTag, "Location is null")
                    return@getLocation
                }

                addMarker(
                    MapMarkerData(
                        location,
                        "Your location",
                        "desc",
                        URL("https://hips.hearstapps.com/hmg-prod/images/high-angle-view-of-variety-of-succulent-plants-royalty-free-image-1584462052.jpg")
                    )
                )   // TODO(remove after testing)

                val currentLocation = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            }
        }
    }

    fun addMarker(markerData: MapMarkerData) {
        val location = LatLng(markerData.location.latitude, markerData.location.longitude)

        val marker = googleMap.addMarker(
            MarkerOptions().position(location).title(markerData.title)
                .snippet(markerData.description)
        )

        marker?.tag = markerData.imageURL.toString()
    }
}
