package com.example.plantidentificationapp.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StructuredName(
    val genus: String?,
    val species: String?
) : Parcelable