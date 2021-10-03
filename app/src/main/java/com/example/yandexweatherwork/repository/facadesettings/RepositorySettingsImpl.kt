package com.example.yandexweatherwork.repository.facadesettings

import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.repository.facadesettings.room.HistoryDAO
import com.example.yandexweatherwork.repository.facadesettings.room.convertDataWeatherToHistoryEntity
import com.example.yandexweatherwork.repository.facadesettings.room.convertHistoryEntityToDataWeather

class RepositorySettingsImpl (private val localDataSource: HistoryDAO): RepositorySettings {
    // Получение полного списка погодных данных
    override fun getAllHistory(): List<DataWeather> {
        return convertHistoryEntityToDataWeather(localDataSource.all())
    }

    // Сохранение новой погодной записи
    override fun saveEntity(dataWeather: DataWeather) {
        localDataSource.insert(convertDataWeatherToHistoryEntity(dataWeather))
    }

    // Удаление записи по ID
    override fun deleteEntity(idForDelete: Long) {
        localDataSource.deleteQ(idForDelete)
    }

    // Получение всей погодной истории данного места (города)
    override fun getDataInHistory(cityName: String): List<DataWeather> {
        return convertHistoryEntityToDataWeather(localDataSource.getDataByWord(cityName))
    }

    // Удалить все записи в таблице
    override fun deleteAll() {
        localDataSource.deleteAll()
    }

    // Обновить имя места (города) в таблице
    override fun updateNameById(idForUpdate: Long, newName: String) {
        localDataSource.updateDataById(idForUpdate, newName)
    }

    // Вывести уникальный список мест, по которым есть погодные записи
    override fun getUniqueListCities(): List<String> {
        return localDataSource.getUniqueListCities()
    }
}