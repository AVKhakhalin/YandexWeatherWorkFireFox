package com.example.yandexweatherwork.domain.core

import android.os.Parcelable
import com.example.yandexweatherwork.domain.ConstantsDomain
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataSettings
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.data.Fact
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class MainChooser() : Parcelable {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var dataWeather: DataWeather? = DataWeather()
    private var dataSettings: DataSettings? = null
    private var knownCities: MutableList<City>? = mutableListOf<City>()
    private var positionCurrentKnownCity: Int = ConstantsDomain.DEFAULT_POSITION_CURRENT_KNOWN_CITY
    private var defaultFilterCity: String = ConstantsDomain.DEFAULT_FILTER_CITY
    private var defaultFilterCountry: String = ConstantsDomain.DEFAULT_FILTER_COUNTRY
    private var fact: Fact? = null
    private var userCorrectedCityList: Boolean = false

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private var existInternet: Boolean = false
    //endregion

    //region ФУНКЦИИ ДЛЯ УСТАНОВКИ ПРИЗНАКА НАЛИЧИЯ ИНТЕРНЕТА
    fun setExistInternet(existInternet: Boolean) {
        this.existInternet = existInternet
    }
    fun getExistInternet(): Boolean = existInternet
    //endregion

    //region ФУНКЦИИ ДЛЯ РАБОТЫ С КООРДИНАТАМИ МЕСТА
    fun setLat(lat: Double) {
        this.lat = lat
    }
    fun setLon(lon: Double) {
        this.lon = lon
    }
    fun getLat(): Double = lat
    fun getLon(): Double = lon
    //endregion

    // Редактирование места (города) в списке
    fun editCity(filterCity: String, filterCountry: String, city: City): Boolean {
        setPositionCurrentKnownCity(filterCity, filterCountry)
        if (positionCurrentKnownCity > -1) {
            knownCities?.let { it.set(positionCurrentKnownCity, city) }
            defaultFilterCity = city.name
            if (city.country.lowercase() == ConstantsDomain.FILTER_RUSSIA.lowercase()) {
                defaultFilterCountry = city.country
            } else {
                defaultFilterCountry = ConstantsDomain.FILTER_NOT_RUSSIA
            }
            positionCurrentKnownCity = positionCurrentKnownCity
            return true
        } else {
            return false
        }
    }

    // Удаление места (города) из списка
    fun removeCity(filterCity: String, filterCountry: String): Boolean {
        setPositionCurrentKnownCity(filterCity, filterCountry)
        if (positionCurrentKnownCity > -1) {
            knownCities?.let { it.removeAt(positionCurrentKnownCity) }
            defaultFilterCity = ConstantsDomain.DEFAULT_FILTER_CITY
            defaultFilterCountry = filterCountry
            positionCurrentKnownCity = -1
            return true
        } else {
            return false
        }
    }

    // Установка признака изменения пользователем списка мест (городов)
    fun setUserCorrectedCityList(userCorrectedCityList: Boolean) {
        this.userCorrectedCityList = userCorrectedCityList
    }

    // Получение значения признака изменения пользователем списка мест (городов)
    fun getUserCorrectedCityList(): Boolean {
        return userCorrectedCityList
    }

    // Установка начальных городов
    fun initKnownCities() {
        knownCities?.apply {
            add(City("Москва", 55.755826, 37.617299900000035, "Россия"))
            add(City("Санкт-Петербург", 59.9342802, 30.335098600000038,
                "Россия"))
            add(City("Новосибирск", 55.00835259999999, 82.93573270000002,
                "Россия"))
            add(City("Екатеринбург", 56.83892609999999, 60.60570250000001,
                "Россия"))
            add(City("Нижний Новгород", 56.2965039, 43.936059, "Россия"))
            add(City("Казань", 55.8304307, 49.06608060000008, "Россия"))
            add(City("Челябинск", 55.1644419, 61.4368432, "Россия"))
            add(City("Омск", 54.9884804, 73.32423610000001, "Россия"))
            add(City("Ростов-на-Дону", 47.2357137, 39.701505, "Россия"))
            add(City("Уфа", 54.7387621, 55.972055400000045, "Россия"))
            add(City("Лондон", 51.5085300, -0.1257400, "Великобритания"))
            add(City("Токио", 35.6895000, 139.6917100, "Япония"))
            add(City("Париж", 48.8534100, 2.3488000, "Франция"))
            add(City("Берлин", 52.52000659999999, 13.404953999999975,
                "Германия"))
            add(City("Рим", 41.9027835, 12.496365500000024, "Италия"))
            add(City("Минск", 53.90453979999999, 27.561524400000053,
                "Белоруссия"))
            add(City("Стамбул", 41.0082376, 28.97835889999999, "Турция"))
            add(City("Вашингтон", 38.9071923, -77.03687070000001, "США"))
            add(City("Киев", 50.4501, 30.523400000000038, "Украина"))
            add(City("Пекин", 39.90419989999999, 116.40739630000007, "Китай"))
        }
    }

    // Установка фильтра выбора места (города) по-умолчанию
    fun setDefaultFilterCity(defaultFilterCity: String) {
        this.defaultFilterCity = defaultFilterCity
    }

    // Получение фильтра выбора места (города) по-умолчанию
    fun getDefaultFilterCity(): String {
        return defaultFilterCity
    }

    // Установка фильтра выбора страны по-умолчанию
    fun setDefaultFilterCountry(defaultFilterCountry: String) {
        this.defaultFilterCountry = defaultFilterCountry
    }

    // Получение фильтра выбора страны по-умолчанию
    fun getDefaultFilterCountry(): String {
        return defaultFilterCountry
    }

    // Получение данных об известном городе, по которому последний раз запрошены погодные данные
    // или который выбран в списке известных городов
    fun getCurrentKnownCity(): City? {
        if ((positionCurrentKnownCity > -1) && (knownCities != null)) {
            return knownCities?.get(positionCurrentKnownCity)!!
        } else {
            return City(ConstantsDomain.ERROR_NAME_CITY, ConstantsDomain.ERROR_CITY_LATIDUTE,
                ConstantsDomain.ERROR_CITY_LONGITUDE, ConstantsDomain.ERROR_COUNTRY)
        }
    }

    // Получение позиции известного города, по которому последний раз запрошены погодные данные
    fun getPositionCurrentKnownCity(): Int {
        if (positionCurrentKnownCity > -1) {
            knownCities?.let {
                defaultFilterCity = it[positionCurrentKnownCity].name
                defaultFilterCountry = it[positionCurrentKnownCity].country
            }
        }
        return positionCurrentKnownCity
    }

    //region Методы установки позиции известного города, по которому последний раз
    // запрошены погодные данные
    fun setPositionCurrentKnownCity(filterCity: String, filterCountry: String) {
        if (knownCities != null) {
            knownCities?.forEachIndexed() { position, city ->
                if ((city.country.lowercase() == filterCountry.lowercase())
                    && (city.name.lowercase() == filterCity.lowercase())) {
                    defaultFilterCity = city.name
                    defaultFilterCountry = city.country
                    positionCurrentKnownCity = position
                    return
                }
            }
            // Установка начальных значений фильтров
            defaultFilterCountry = ""
            positionCurrentKnownCity = -1
            if (defaultFilterCountry.lowercase() != ConstantsDomain.FILTER_RUSSIA.lowercase()) {
                defaultFilterCountry = ConstantsDomain.FILTER_NOT_RUSSIA
            }
        }
    }
    fun setPositionCurrentKnownCity(position: Int) {
        if (knownCities != null) {
            if (position > -1) {
                knownCities?.let {
                    defaultFilterCity = it[position].name
                    defaultFilterCountry = it[position].country
                }
            } else {
                // Установка начальных значений фильтров
                defaultFilterCity = ConstantsDomain.DEFAULT_FILTER_CITY
                if (defaultFilterCountry.lowercase() != ConstantsDomain.FILTER_RUSSIA.lowercase()) {
                    defaultFilterCountry = ConstantsDomain.FILTER_NOT_RUSSIA
                }
            }
            positionCurrentKnownCity = position
        }
    }
    //endregion

    //region МЕТОДЫ ДЛЯ ПОЛУЧЕНИЯ СПИСКА ИЗВЕСТНЫХ ГОРОДОВ
    fun getKnownCities(filterCity: String, filterCountry: String): MutableList<City>? {
        return analiseKnownCities(filterCity, filterCountry)
    }
    fun getKnownCities(): MutableList<City>? {
        val filterCity: String = defaultFilterCity
        val filterCountry: String = defaultFilterCountry
        return analiseKnownCities(filterCity, filterCountry)
    }
    private fun analiseKnownCities(filterCity: String, filterCountry: String): MutableList<City>? {
        if ((filterCity == null) || (filterCountry == null)) {
            return mutableListOf(City(ConstantsDomain.ERROR_NAME_CITY,
                ConstantsDomain.ERROR_CITY_LATIDUTE, ConstantsDomain.ERROR_CITY_LONGITUDE,
                ConstantsDomain.ERROR_COUNTRY))
        } else {
            // Корректировка фильтров места (города) и страны
            val newFilterCountry = filterCountry
            var newKnownCities: MutableList<City>? = mutableListOf()
            // Фильтрация и построение списка мест (городов)
            if (newFilterCountry == "") {
                // Фильтрация только ПО НАЗВАНИЮ ГОРОДА
                return knownCities?.run {
                    forEach { city ->
                        if ((filterCity == "") || (city.name.lowercase() == filterCity.lowercase())
                            || (city.name.lowercase().indexOf(
                                filterCity.lowercase()
                            ) > -1)
                        ) {
                            if (newKnownCities == null) {
                                newKnownCities = mutableListOf(city)
                            } else {
                                newKnownCities?.add(city)
                            }
                        }
                    }
                    newKnownCities
                }
            } else {
                // Фильтрация в случае ИСКЛЮЧЕНИЯ СТРАНЫ из списка
                if ((newFilterCountry.length > 1) && (newFilterCountry.indexOf("-") == 0)) {
                    if (knownCities != null) {
                        val newFilterCountry: String = newFilterCountry.substring(1)
                        return knownCities?.run{
                            forEach { city ->
                                    if (city.country.lowercase() != newFilterCountry.lowercase()
                                        && (filterCity == ""
                                                || city.name.lowercase() == filterCity.lowercase()
                                                || (city.name.lowercase().indexOf(
                                            filterCity.lowercase()
                                        ) > -1))) {
                                    if (newKnownCities == null) {
                                        newKnownCities = mutableListOf(city)
                                    } else {
                                        newKnownCities?.add(city)
                                    }
                                }
                            }
                            newKnownCities
                        }
                    } else {
                        return mutableListOf(City(ConstantsDomain.ERROR_NAME_CITY,
                                                  ConstantsDomain.ERROR_CITY_LATIDUTE,
                                                  ConstantsDomain.ERROR_CITY_LONGITUDE,
                                                  ConstantsDomain.ERROR_COUNTRY))
                    }
                } else {
                    // Фильтрация в случае поиска ПО НАЗВАНИЯМ СТРАНЫ И ГОРОДА
                    if (knownCities != null) {
                        return knownCities?.run{
                            forEach { city ->
                                if ((city.country.lowercase() == newFilterCountry.lowercase())
                                    && ((filterCity == "")
                                            || (city.name.lowercase() == filterCity.lowercase())
                                            || (city.name.lowercase()
                                        .indexOf(filterCity.lowercase()) > -1))) {
                                    if (newKnownCities == null) {
                                        newKnownCities = mutableListOf(city)
                                    } else {
                                        newKnownCities?.add(city)
                                    }
                                }
                            }
                            newKnownCities
                        }
                    } else {
                        return mutableListOf(City(ConstantsDomain.ERROR_NAME_CITY,
                                                  ConstantsDomain.ERROR_CITY_LATIDUTE,
                                                  ConstantsDomain.ERROR_CITY_LONGITUDE,
                                                  ConstantsDomain.ERROR_COUNTRY))
                    }
                }
            }
        }
    }
    //endregion

    // Добавить новый город в список известных городов
    fun addKnownCities(city: City) {
        if (knownCities == null) {
            knownCities = mutableListOf(city)
        } else {
            knownCities?.add(city)
        }
    }

    // Получить количество известных городов
    fun getNumberKnownCities(): Int {
        return if (knownCities == null) {
            0
        } else {
            knownCities!!.size
        }
    }

    // Получить данные о погоде сейчас
    fun getDataWeather(): DataWeather? {
        return dataWeather
    }

    // Установить фактические данные о погоде
    fun setFact(fact: Fact?, lat: Double, lon: Double, error: Throwable?) {
        this.fact = fact
        if (fact != null) {
            dataWeather?.let{
                it.city = City(getCurrentKnownCity()!!.name, lat, lon,
                    getCurrentKnownCity()!!.country)
                it.time = Date().toString()
                it.temperature = fact.temp
                it.feelsLike = fact.feels_like
                it.tempWater = fact.temp_water
                it.iconCode = fact.icon
                it.conditionCode = fact.condition
                it.windSpeed = fact.wind_speed
                it.windGust = fact.wind_gust
                it.windDirection = fact.wind_dir
                it.mmPresure = fact.pressure_mm
                it.paPressure = fact.pressure_pa
                it.humidity = fact.humidity
                it.dayTime = fact.daytime
                it.polar = fact.polar
                it.season = fact.season
                it.error = error
            }
        } else {
            dataWeather?.let{
                it.city = null
                it.time = null
                it.temperature = null
                it.feelsLike = null
                it.tempWater = null
                it.iconCode = null
                it.conditionCode = null
                it.windSpeed = null
                it.windGust = null
                it.windDirection = null
                it.mmPresure = null
                it.paPressure = null
                it.humidity = null
                it.dayTime = null
                it.polar = null
                it.season = null
                it.error = error
            }
        }
    }
}