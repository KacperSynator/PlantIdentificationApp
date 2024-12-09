package com.example.plantidentificationapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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
            setupMarkerClick(googleMap)
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

    private fun setupMarkerClick(googleMap: GoogleMap) {
        googleMap.setOnMarkerClickListener { marker ->
            // Inflate the custom layout
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.marker_popup_window, null)

            // Find views in the popup layout
            val titleTextView = popupView.findViewById<TextView>(R.id.popup_title)
            val imageView = popupView.findViewById<ImageView>(R.id.popup_image)
            val descriptionTextView = popupView.findViewById<TextView>(R.id.popup_description)

            // Set marker data to the popup views
            titleTextView.text = marker.title
            descriptionTextView.text = marker.snippet

            // Assuming marker.tag contains the image URL
            val imageUrl = marker.tag as? String
            if (imageUrl != null) {
                Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.loading)
                    .into(imageView)
            }

            // Create and show the dialog
            val dialog = AlertDialog.Builder(context)
                .setView(popupView)
                .setCancelable(true)
                .create()

            dialog.show()

            // Return true to indicate that the click was handled
            true
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
