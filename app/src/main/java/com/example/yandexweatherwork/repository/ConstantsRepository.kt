package com.example.yandexweatherwork.repository

import com.example.yandexweatherwork.BuildConfig
import com.example.yandexweatherwork.controller.ConstantsController

class ConstantsRepository {
    companion object {
        // Константы для получения данных о погоде
        @JvmField
        val BASE_URL : String = "https://api.weather.yandex.ru/"
        @JvmField
        val END_POINT : String = "v2/informers"
        @JvmField
        val YANDEX_KEY_TITLE : String = "X-Yandex-API-Key"
        @JvmField
//        val YANDEX_KEY_VALUE : String = BuildConfig.WEATHER_API_KEY
        // Оставил этот ключ как есть, чтобы Вы могли тестировать проект
        val YANDEX_KEY_VALUE : String = "ebbee072-d212-420e-9f62-4d716b0499e9"
        @JvmField
        val LATITUDE_NAME : String = "lat"
        @JvmField
        val LONGITUDE_NAME : String = "lon"
        @JvmField
        val LANGUAGE: String = "lang"
        @JvmField
        val INFO_RESPONSE: String = "response"
        @JvmField
        val BROADCAST_ACTION: String = "android.net.conn.CONNECTIVITY_CHANGE"
        @JvmField
        val GOOGLE_URL_TO_PING: String = "https://google.com"
        @JvmField
        val INTERNET_SERVICE_STRING_KEY = "internetServiceStringKey"
        @JvmField
        val MAIN_CHOOSER_GETTER: String = ConstantsController.MAIN_CHOOSER_GETTER
        @JvmField
        val MAIN_CHOOSER_SETTER: String = ConstantsController.MAIN_CHOOSER_SETTER
        @JvmField
        val WEATHER_DATA_INTENT_FILTER: String = ConstantsController.WEATHER_DATA_INTENT_FILTER
        @JvmField
        val WEATHER_DATA: String = "WEATHER_DATA"
        @JvmField
        val DATES_LOADED: String = ConstantsController.DATES_LOADED

        // Константы для локальных настроек программы

    }
}