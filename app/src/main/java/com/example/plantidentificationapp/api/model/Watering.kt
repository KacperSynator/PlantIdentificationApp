package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Watering(
    val max: Int,
    val min: Int
) : Parcelable