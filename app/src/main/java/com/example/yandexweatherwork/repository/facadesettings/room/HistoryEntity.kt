package com.example.yandexweatherwork.repository.facadesettings.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    var lat: Double,
    var lon: Double,
    val country: String,
    var temperature: Float,
    var feelsLike: Float,
    var tempWater: Float,
    var iconCode: String,
    var conditionCode: String,
    var windSpeed: Float,
    var windGust: Float,
    var windDirection: String,
    var mmPresure: Float,
    var paPressure: Float,
    var humidity: Float,
    var dayTime: String,
    var polar: Boolean,
    var season: String
)