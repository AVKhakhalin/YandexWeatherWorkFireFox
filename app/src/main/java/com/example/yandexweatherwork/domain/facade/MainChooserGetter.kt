package com.example.yandexweatherwork.domain.facade

import android.os.Parcelable
import com.example.yandexweatherwork.domain.core.MainChooser
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataWeather
import kotlinx.android.parcel.Parcelize

@Parcelize
class MainChooserGetter(private val mainChooser: MainChooser): Parcelable {
    // Получение данных о погоде
    fun getDataWeather(): DataWeather? = mainChooser.getDataWeather()

    // Получение количества известных мест (городов)
    fun getNumberKnownCites(): Int = mainChooser.getNumberKnownCities()

    //region МЕТОДЫ ПОЛУЧЕНИЯ СПИСКА ИЗВЕСТНЫХ МЕСТ (ГОРОДОВ)
    fun getKnownCites(filterCity: String, filterCountry: String): MutableList<City>? =
        mainChooser.getKnownCities(filterCity, filterCountry)

    fun getKnownCites(): MutableList<City>? = mainChooser.getKnownCities()
    //endregion

    // Получение данных об известном городе, по которому последний раз запрошены погодные данные
    // или который выбран в списке известных городов
    fun getCurrentKnownCity(): City? = mainChooser.getCurrentKnownCity()

    // Получение позиции известного города, по которому последний раз запрошены погодные данные
    fun getPositionCurrentKnownCity(): Int = mainChooser.getPositionCurrentKnownCity()

    // Получение фильтра выбора места (города) по-умолчанию
    fun getDefaultFilterCity(): String = mainChooser.getDefaultFilterCity()

    // Получение фильтра выбора страны по-умолчанию
    fun getDefaultFilterCountry(): String = mainChooser.getDefaultFilterCountry()

    // Получение значения признака изменения пользователем списка мест (городов)
    fun getUserCorrectedCityList(): Boolean = mainChooser.getUserCorrectedCityList()

    //region МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ СКОРРЕКТИРОВАННЫХ КООРДИНАТ
    fun getLat(): Double = mainChooser.getLat()
    fun getLon(): Double = mainChooser.getLon()
    //endregion

    //Функция для получения признака наличия Интернета
    fun getExistInternet(): Boolean = mainChooser.getExistInternet()
}