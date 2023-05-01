package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Suggestion(
    val confirmed: Boolean,
    val id: Int,
    val plant_details: PlantDetails,
    val plant_name: String,
    val probability: Double,
    val similar_images: List<SimilarImage>?
) : Parcelable