package com.example.yandexweatherwork.domain.facade

import android.os.Parcelable
import com.example.yandexweatherwork.domain.core.MainChooser
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataModel
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.data.Fact
import com.example.yandexweatherwork.repository.facadesettings.RepositorySettingsImpl
import kotlinx.android.parcel.Parcelize

@Parcelize
class MainChooserSetter(private val mainChooser: MainChooser): Parcelable {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var dataModel: DataModel? = null
    //endregion

    //region МЕТОДЫ ДЛЯ УСТАНОВКИ СКОРРЕКТИРОВАННЫХ КООРДИНАТ
    fun setLat(lat: Double) = mainChooser?.let{it.setLat(lat)}
    fun setLon(lon: Double) = mainChooser?.let{it.setLon(lon)}
    //endregion

    // Установить данные о погоде сейчас (полученные из базы данных Room)
    fun setDataWeather(dataWeather: DataWeather) = mainChooser?.let{it.setDataWeather(dataWeather)}

    // Установить признак считывания погодных данных из базы данных
    fun setIsDataWeatherFromLocalBase(isDataWeatherFromLocalBase: Boolean) =
        mainChooser?.let {it.setIsDataWeatherFromLocalBase(isDataWeatherFromLocalBase)}

    //region Методы для Передачи полученных данных в MainChooser
    fun setDataModel(
        dataModel: DataModel?,
        lat: Double,
        lon: Double,
        error: Throwable?
    ) {
        this.dataModel = dataModel
        if (dataModel != null) {
            setFact(dataModel.fact, lat, lon, error)
        } else {
            setFact(null, lat, lon, error)
        }
    }
    private fun setFact(fact: Fact?, lat: Double, lon: Double, error: Throwable?) {
        mainChooser?.let{
            it.setFact(fact, lat, lon, error)
        }
    }
    //endregion


    // Добавление новго известного места (города) в список известных мест (городов)
    fun addKnownCities(city: City) = mainChooser?.let {it.addKnownCities(city)}

    // Установка фильтра выбора места (города) по-умолчанию
    fun setDefaultFilterCity(defaultFilterCity: String) = mainChooser?.let {
        it.setDefaultFilterCity(defaultFilterCity)}

    // Установка фильтра выбора страны по-умолчанию
    fun setDefaultFilterCountry(defaultFilterCountry: String) = mainChooser?.let {
        it.setDefaultFilterCountry(defaultFilterCountry)}

    //region Методы установки позиции известного города, по которому последний раз запрошены погодные данные
    fun setPositionCurrentKnownCity(filterCity: String, filterCountry: String) = mainChooser?.let {
        it.setPositionCurrentKnownCity(filterCity, filterCountry)}

    fun setPositionCurrentKnownCity(position: Int) = mainChooser?.let {
        it.setPositionCurrentKnownCity(position)}
    //region

    // Установка начальных городов
    fun initKnownCities() = mainChooser?.let {it.initKnownCities()}

    // Установка признака изменения пользователем списка мест (городов)
    fun setUserCorrectedCityList(userCorrectedCityList: Boolean) =
        mainChooser?.let{it.setUserCorrectedCityList(userCorrectedCityList)}

    // Удаление места (города) из списка городов
    fun removeCity(filterCity: String, filterCountry: String): Boolean {
        mainChooser?.let {
            if (it.removeCity(filterCity, filterCountry))
                return true
        }
        return false
    }

    // Редактирование места (города) в списке
    fun editCity(filterCity: String, filterCountry: String, city: City): Boolean {
        mainChooser?.let {
            return it.editCity(filterCity, filterCountry, city)
        }
        return false
    }

    //Функция для установки признака наличия Интернета
    fun setExistInternet(existInternet: Boolean) {
        mainChooser?.let {
            it.setExistInternet(existInternet)
        }
    }
}