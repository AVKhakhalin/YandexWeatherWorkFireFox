package com.example.yandexweatherwork.repository.facadesettings

import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.repository.facadesettings.room.HistoryDAO
import com.example.yandexweatherwork.repository.facadesettings.room.convertDataWeatherToHistoryEntity
import com.example.yandexweatherwork.repository.facadesettings.room.convertHistoryEntityToDataWeather

class RepositorySettingsImpl (private val localDataSource: HistoryDAO): RepositorySettings {
    override fun getAllHistory(): List<DataWeather> {
        return convertHistoryEntityToDataWeather(localDataSource.all())
    }

    override fun saveEntity(dataWeather: DataWeather) {
        localDataSource.insert(convertDataWeatherToHistoryEntity(dataWeather))
    }

    override fun deleteEntity(idForDelete: Long) {
        localDataSource.deleteQ(idForDelete)
    }

    override fun getDataInHistory(name: String): List<DataWeather> {
        return convertHistoryEntityToDataWeather(localDataSource.getDataByWord(name))
    }

    override fun deleteAll() {
        localDataSource.deleteAll()
    }

    override fun updateNameById(idForUpdate: Long, newName: String) {
        localDataSource.updateDataById(idForUpdate, newName)
    }

    override fun getUniqueListCities(): List<String> {
        return localDataSource.getUniqueListCities()
    }
}