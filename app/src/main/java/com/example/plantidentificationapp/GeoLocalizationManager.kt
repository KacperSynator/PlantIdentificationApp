package com.example.plantidentificationapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GeoLocalizationManager(private val context: Context) {
    private val logTag = "GeoLocalizationManager"

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getLocation(callback: (Location?) -> Unit) {
        if (!isPermissionGranted()) {
            Log.i(logTag, "Permission is not granted, asking for permission")
            requestPermission()
        }

        if (isPermissionGranted()) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.i(logTag, "Location found Lat: ${location.latitude}, Long: ${location.longitude}")
                    } else {
                        Log.e(logTag, "Location is null")
                    }
                    callback(location)
                }
                .addOnFailureListener { e: Exception ->
                    Log.e(logTag, "Failed to read geolocation: ${e.message}")
                    callback(null)
                }
        } else {
            Log.e(logTag, "Permission is still not granted")
            callback(null)
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
