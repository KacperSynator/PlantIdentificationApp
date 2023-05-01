package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WikiImage(
    val citation: String,
    val license_name: String,
    val license_url: String,
    val value: String
) : Parcelable