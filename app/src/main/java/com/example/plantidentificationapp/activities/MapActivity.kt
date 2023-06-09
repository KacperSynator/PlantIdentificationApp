package com.example.plantidentificationapp.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.plantidentificationapp.GoogleMapManager
import com.example.plantidentificationapp.R
import com.google.android.gms.maps.MapView


class MapActivity : AppCompatActivity() {
    private val logTag = "MapActivity"

    private lateinit var mapView: MapView

    private var mapManager: GoogleMapManager = GoogleMapManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_screen)

        requestInternetPermission()

        mapView = findViewById(R.id.google_map)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(mapManager)
    }

    private fun requestInternetPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            Log.i(logTag, "Asking for Internet permission")
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.INTERNET),
                MY_PERMISSIONS_INTERNET
            )
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        const val MY_PERMISSIONS_INTERNET = 1
    }
}
