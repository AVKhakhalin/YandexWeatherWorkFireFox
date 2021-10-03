package com.example.yandexweatherwork.repository

import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.domain.ConstantsDomain
import com.example.yandexweatherwork.ui.ConstantsUi

class ConstantsRepository {
    companion object {
        // Константы для получения данных о погоде
        @JvmField
        val BASE_URL: String = "https://api.weather.yandex.ru/"
        @JvmField
        val END_POINT: String = "v2/informers"
        @JvmField
        val YANDEX_KEY_TITLE: String = "X-Yandex-API-Key"
        @JvmField
//        val YANDEX_KEY_VALUE : String = BuildConfig.WEATHER_API_KEY
        // Оставил этот ключ как есть, чтобы Вы могли тестировать проект
//        val YANDEX_KEY_VALUE : String = "ebbee072-d212-420e-9f62-4d716b0499e9"
        val YANDEX_KEY_VALUE: String = "ZWJiZ WUwN zItZ DIxM i00M jBlL TlmN jItN GQ3M TZiM DQ5O WU5${Char(10)} "
        @JvmField
        val LATITUDE_NAME: String = "lat"
        @JvmField
        val LONGITUDE_NAME: String = "lon"
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

        // Константы для приёмника внешних сообщний о новом месте
        @JvmField
        val NAME_MSG_CITY_NAME: String = "MSG_CITY_NAME"
        @JvmField
        val NAME_MSG_CITY_LAT: String = "MSG_CITY_LAT"
        @JvmField
        val NAME_MSG_CITY_LON: String = "MSG_CITY_LON"
        @JvmField
        val NAME_MSG_CITY_COUNTRY: String = "MSG_CITY_COUNTRY"
        @JvmField
        val TAG: String = "MessageBroadcastReceiver"
        @JvmField
        val BROADCAST_ACTION_NEW_CITY: String = "broadcastsender.city"
        @JvmField
        val ERROR_COORDINATE: Double = ConstantsUi.ERROR_COORDINATE
        @JvmField
        val CITY_IMAGE_LINK: String = "https://freepngimg.com/thumb/city/36275-3-city-hd.png"
        @JvmField
        val ERROR_NAME_CITY: String = ConstantsDomain.ERROR_NAME_CITY
        @JvmField
        val ERROR_CITY_LATIDUTE: Double = ConstantsDomain.ERROR_CITY_LATIDUTE
        @JvmField
        val ERROR_CITY_LONGITUDE: Double = ConstantsDomain.ERROR_CITY_LONGITUDE
        @JvmField
        val ERROR_COUNTRY: String = ConstantsDomain.ERROR_COUNTRY
        @JvmField
        val ERROR_STRING: String = "Error_Database_Data"
        @JvmField
        val ERROR_TIME: String = "Error_time"

        // Константы для локальных настроек программы

    }
}