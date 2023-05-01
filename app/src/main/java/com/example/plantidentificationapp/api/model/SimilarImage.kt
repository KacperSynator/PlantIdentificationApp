package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SimilarImage(
    val id: String?,
    val similarity: Double?,
    val url: String?,
    val url_small: String?
) : Parcelable