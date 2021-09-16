package com.example.yandexweatherwork.repository.facadeuser

import com.example.yandexweatherwork.domain.data.DataWeather

interface RepositoryWeather {
    fun getWeatherFromRemoteSource(lat: Double, lon: Double, lang: String)
    fun getWeatherFromLocalSource() : DataWeather
}