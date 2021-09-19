package com.example.yandexweatherwork.domain

class ConstantsDomain {
    companion object {
        @JvmField
        val FILTER_RUSSIA: String = "Россия"
        @JvmField
        val FILTER_NOT_RUSSIA: String = "-Россия"
        @JvmField
        val DEFAULT_POSITION_CURRENT_KNOWN_CITY: Int = -1
        @JvmField
        val DEFAULT_FILTER_CITY: String = ""
        @JvmField
        val DEFAULT_FILTER_COUNTRY: String = ""
        @JvmField
        val ERROR_NAME_CITY: String = "Москва(Error)"
        @JvmField
        val ERROR_CITY_LATIDUTE: Double = 55.7522
        @JvmField
        val ERROR_CITY_LONGITUDE: Double = 37.6156
        @JvmField
        val ERROR_COUNTRY: String = "Россия(Error)"

    }
}