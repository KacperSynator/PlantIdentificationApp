package com.example.plantidentificationapp.classes

import android.location.Location
import java.net.URL

data class MapMarkerData(
    val location: Location,
    val title: String,
    val description: String,
    val imageURL: URL
)
