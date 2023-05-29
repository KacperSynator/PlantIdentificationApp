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

class GoogleMapManager(private val context: Context) : OnMapReadyCallback {
    private val logTag = "GoogleMapManager"

    private lateinit var googleMap: GoogleMap

    private var geoManager: GeoLocalizationManager = GeoLocalizationManager(context)
    private var databaseManager: DatabaseManager = DatabaseManager()


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

                val zoomLevel = 15f;
                val currentLocation = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel))

                loadAllMapMarkers()
            }
        }
    }

    private fun addMarker(markerData: MapMarkerData) {
        val latLng = LatLng(markerData.location!!.latitude, markerData.location.longitude)

        val google = googleMap.addMarker(
            MarkerOptions().position(latLng).title(markerData.title)
                .snippet(markerData.description)
        )

        google?.tag = markerData.imageURL.toString()
    }

    private fun loadAllMapMarkers() {
        databaseManager.getAllPlants { markers ->
            for (marker in markers) {
                addMarker(marker)
            }
        }
    }
}
