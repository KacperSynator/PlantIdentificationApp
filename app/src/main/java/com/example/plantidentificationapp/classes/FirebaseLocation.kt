package com.example.plantidentificationapp.classes

import android.location.Location

data class FirebaseLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    constructor(location: Location) : this(location.latitude, location.longitude)
}
