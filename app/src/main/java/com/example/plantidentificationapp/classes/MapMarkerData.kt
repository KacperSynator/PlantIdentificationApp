package com.example.plantidentificationapp.classes

import android.location.Location

data class MapMarkerData(
    val location: FirebaseLocation? = null,
    val title: String? = null,
    val description: String? = null,
    val imageURL: String? = null
) {
    // Add a no-argument constructor
    constructor() : this(null, null, null, null)
}
