package com.example.yandexweatherwork.repository.facadesettings.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    var name: String,
    var lat: Double,
    var lon: Double,
    var country: String,
    var time: String,
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