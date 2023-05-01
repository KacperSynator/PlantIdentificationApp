package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Taxonomy(
    val `class`: String?,
    val family: String?,
    val genus: String?,
    val kingdom: String?,
    val order: String?,
    val phylum: String?
) : Parcelable