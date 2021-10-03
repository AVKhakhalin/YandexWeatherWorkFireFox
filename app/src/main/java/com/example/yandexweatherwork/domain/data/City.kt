package com.example.yandexweatherwork.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class City (
    val name: String,
    var lat: Double,
    var lon: Double,
    val country: String
): Parcelable