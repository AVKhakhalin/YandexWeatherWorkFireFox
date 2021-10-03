package com.example.yandexweatherwork.repository.facadesettings

import com.example.yandexweatherwork.domain.data.DataWeather

interface RepositorySettings {
    fun getAllHistory(): List<DataWeather>
    fun saveEntity(dataWeather: DataWeather)
    fun deleteEntity(idForDelete: Long)
    fun getDataInHistory(name: String): List<DataWeather>
    fun deleteAll()
    fun updateNameById(idForUpdate: Long, newName: String)
}