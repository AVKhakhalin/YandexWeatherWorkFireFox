package com.example.yandexweatherwork.repository.facadesettings.room

import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.repository.ConstantsRepository

fun convertHistoryEntityToDataWeather(entityList: List<HistoryEntity>): List<DataWeather>{
    return entityList.map {
        DataWeather(
            City(it.name,it.lat,it.lon, it.country),
            it.temperature,
            it.feelsLike,
            it.tempWater,
            it.iconCode,
            it.conditionCode,
            it.windSpeed,
            it.windGust,
            it.windDirection,
            it.mmPresure,
            it.paPressure,
            it.humidity,
            it.dayTime,
            it.polar,
            it.season,
            null)
    }
}

fun convertDataWeatherToHistoryEntity(dataWeather: DataWeather): HistoryEntity{
    return HistoryEntity(
        0,
        if (dataWeather.city != null) dataWeather.city!!.name
        else ConstantsRepository.ERROR_NAME_CITY,
        if (dataWeather.city != null) dataWeather.city!!.lat
        else ConstantsRepository.ERROR_CITY_LATIDUTE,
        if (dataWeather.city != null) dataWeather.city!!.lon
        else ConstantsRepository.ERROR_CITY_LONGITUDE,
        if (dataWeather.city != null) dataWeather.city!!.country
        else ConstantsRepository.ERROR_COUNTRY,
        if (dataWeather.temperature != null) dataWeather.temperature!!
        else 0f,
        if (dataWeather.feelsLike != null) dataWeather.feelsLike!!
        else 0f,
        if (dataWeather.tempWater != null) dataWeather.tempWater!!
        else 0f,
        if (dataWeather.iconCode != null) dataWeather.iconCode!!
        else ConstantsRepository.ERROR_STRING,
        if (dataWeather.conditionCode != null) dataWeather.conditionCode!!
        else "skc_n",
        if (dataWeather.windSpeed != null) dataWeather.windSpeed!!
        else 0f,
        if (dataWeather.windGust != null) dataWeather.windGust!!
        else 0f,
        if (dataWeather.windDirection != null) dataWeather.windDirection!!
        else ConstantsRepository.ERROR_STRING,
        if (dataWeather.mmPresure != null) dataWeather.mmPresure!!
        else 0f,
        if (dataWeather.paPressure != null) dataWeather.paPressure!!
        else 0f,
        if (dataWeather.humidity != null) dataWeather.humidity!!
        else 0f,
        if (dataWeather.dayTime != null) dataWeather.dayTime!!
        else ConstantsRepository.ERROR_STRING,
        if (dataWeather.polar != null) dataWeather.polar!!
        else false,
        if (dataWeather.season != null) dataWeather.season!!
        else ConstantsRepository.ERROR_STRING
    )
}