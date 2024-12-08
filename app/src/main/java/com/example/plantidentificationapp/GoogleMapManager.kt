package com.example.plantidentificationapp

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.plantidentificationapp.adapters.MapMarkerInfoWindowAdapter
import com.example.plantidentificationapp.classes.MapMarkerData
import com.example.plantidentificationapp.classes.MapType
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

                val activity = context as? Activity
                val intent = activity?.intent
                val mapType = intent?.getSerializableExtra("mapType") as? MapType

                Log.d(logTag, "Received mapType: $mapType")

                when (mapType!!) {
                    MapType.ALL_USERS -> {loadAllMapMarkers()}
                    MapType.CURRENT_USER -> {loadCurrentUserMarkers()}
                }
            }
        }
    }

    private fun addMarker(markerData: MapMarkerData) {
        val latLng = LatLng(markerData.location!!.latitude, markerData.location.longitude)

        val marker = googleMap.addMarker(
            MarkerOptions().position(latLng).title(markerData.title)
                .snippet(markerData.description)
        )

        marker?.tag = markerData.imageURL.toString()
    }

    private fun loadAllMapMarkers() {
        databaseManager.getAllPlants { markers ->
            for (marker in markers) {
                addMarker(marker)
            }
        }
    }

    private fun loadCurrentUserMarkers() {
        val authManager = AuthenticationManager(context)
        databaseManager.getPlantsForUser(authManager.currentUser()!!) { markers ->
            for (marker in markers) {
                addMarker(marker)
            }
        }
    }
}
