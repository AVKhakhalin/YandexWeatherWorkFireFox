package com.example.yandexweatherwork.domain.core

import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataSettings
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.data.Fact

class MainChooser() {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var dataWeather: DataWeather? = DataWeather()
    private var dataSettings: DataSettings? = null
    private var knownCities: MutableList<City>? = mutableListOf<City>()
    private var positionCurrentKnownCity: Int = -1
    private var defaultFilterCity: String = ""
    private var defaultFilterCountry: String = ""
    private var fact: Fact? = null

    private var lat: Double = 0.0
    private var lon: Double = 0.0
    //endregion

    fun setLat(lat: Double) {
        this.lat = lat
    }
    fun setLon(lon: Double) {
        this.lon = lon
    }
    fun getLat(): Double = lat
    fun getLon(): Double = lon

    // Установка начальных городов
    fun initKnownCities() {
        knownCities?.apply {
            add(City("Москва", 55.755826, 37.617299900000035, "Россия"))
            add(City("Санкт-Петербург", 59.9342802, 30.335098600000038, "Россия"))
            add(City("Новосибирск", 55.00835259999999, 82.93573270000002, "Россия"))
            add(City("Екатеринбург", 56.83892609999999, 60.60570250000001, "Россия"))
            add(City("Нижний Новгород", 56.2965039, 43.936059, "Россия"))
            add(City("Казань", 55.8304307, 49.06608060000008, "Россия"))
            add(City("Челябинск", 55.1644419, 61.4368432, "Россия"))
            add(City("Омск", 54.9884804, 73.32423610000001, "Россия"))
            add(City("Ростов-на-Дону", 47.2357137, 39.701505, "Россия"))
            add(City("Уфа", 54.7387621, 55.972055400000045, "Россия"))
            add(City("Лондон", 51.5085300, -0.1257400, "Великобритания"))
            add(City("Токио", 35.6895000, 139.6917100, "Япония"))
            add(City("Париж", 48.8534100, 2.3488000, "Франция"))
            add(City("Берлин", 52.52000659999999, 13.404953999999975, "Германия"))
            add(City("Рим", 41.9027835, 12.496365500000024, "Италия"))
            add(City("Минск", 53.90453979999999, 27.561524400000053, "Белоруссия"))
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

    // Получение данных об известном городе, по которому последний раз запрошены погодные данные или который выбран в списке известных городов
    fun getCurrentKnownCity(): City? {
        if ((positionCurrentKnownCity > -1) && (knownCities != null)) {
            return knownCities?.get(positionCurrentKnownCity)!!
        } else {
            return City("Москва(ERR)", 55.7522, 37.6156, "Россия(ERR)")
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

    //region Методы установки позиции известного города, по которому последний раз запрошены погодные данные
    fun setPositionCurrentKnownCity(filterCity: String, filterCountry: String) {
        if (knownCities != null) {
            knownCities?.forEachIndexed() { position, city ->
                if ((city.country == filterCountry) && (city.name == filterCity)) {
                    defaultFilterCity = city.name
                    defaultFilterCountry = city.country
                    positionCurrentKnownCity = position
                    return
                }
            }
        }
    }
    fun setPositionCurrentKnownCity(position: Int) {
        if ((positionCurrentKnownCity > -1) && (knownCities != null)) {
            knownCities?.let{
                defaultFilterCity = it[positionCurrentKnownCity].name
                defaultFilterCountry = it[positionCurrentKnownCity].country
            }
        }
        positionCurrentKnownCity = position
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
            return mutableListOf(City("Москва", 55.7522, 37.6156, "Россия"))
        } else {
            // Корректировка фильтров места (города) и страны
            val newFilterCountry = filterCountry
            var newKnownCities: MutableList<City>? = null
            // Фильтрация и построение списка мест (городов)
            if (newFilterCountry == "") {
                // Фильтрация только ПО НАЗВАНИЮ ГОРОДА
                return knownCities?.run {
                    forEach { city ->
                        if ((filterCity == "") || (city.name == filterCity) || (city.name.indexOf(
                                filterCity
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
                                if (city.country != newFilterCountry && (city.country.indexOf(newFilterCountry) == -1) && (filterCity == "" || city.name == filterCity || (city.name.indexOf(
                                        filterCity
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
                        return mutableListOf(City("Москва(ERR)", 55.7522, 37.6156, "Россия(ERR)"))
                    }
                } else {
                    // Фильтрация в случае поиска ПО НАЗВАНИЯМ СТРАНЫ И ГОРОДА
                    if (knownCities != null) {
                        return knownCities?.run{
                            forEach { city ->
                                if ((city.country == newFilterCountry) && ((filterCity == "") || (city.name == filterCity) || (city.name.indexOf(filterCity) > -1))) {
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
                        return mutableListOf(City("Москва(ERR)", 55.7522, 37.6156, "Россия(ERR)"))
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
                it.city = City(getCurrentKnownCity()!!.name, lat, lon, getCurrentKnownCity()!!.country)
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