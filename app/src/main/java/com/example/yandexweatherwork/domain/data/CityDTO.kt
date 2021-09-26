package com.example.yandexweatherwork.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
// Класс для получения информации о месте (координаты, название страны)
data class CityDTO(
    val place_id: Long,
    val licence: String,
    val osm_type: String,
    val osm_id: Long,
    val boundingbox: List<String>,
    val lat: String,
    val lon: String,
    val display_name: String,
    val place_rank: Long,
    val category: String,
    val type: String,
    val importance: Double,
    val icon: String
) : Parcelable
