package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlantDetails(
    val common_names: List<String>?,
    val edible_parts: List<String>?,
    val propagation_methods: List<String>?,
    val scientific_name: String?,
    val structured_name: StructuredName?,
    val synonyms: List<String>?,
    val taxonomy: Taxonomy?,
    val url: String?,
    val watering: Watering?,
    val wiki_description: WikiDescription?,
    val wiki_image: WikiImage?
) : Parcelable