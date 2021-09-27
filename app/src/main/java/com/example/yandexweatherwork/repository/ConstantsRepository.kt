package com.example.yandexweatherwork.repository

import android.provider.SyncStateContract
import com.example.yandexweatherwork.BuildConfig
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.ui.ConstantsUi

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

        // Константы для приёмника внешних сообщний о новом месте
        @JvmField
        val NAME_MSG_CITY_NAME = "MSG_CITY_NAME"
        @JvmField
        val NAME_MSG_CITY_LAT = "MSG_CITY_LAT"
        @JvmField
        val NAME_MSG_CITY_LON = "MSG_CITY_LON"
        @JvmField
        val NAME_MSG_CITY_COUNTRY = "MSG_CITY_COUNTRY"
        @JvmField
        val TAG = "MessageBroadcastReceiver"
        @JvmField
        val BROADCAST_ACTION_NEW_CITY: String = "broadcastsender.city"
        @JvmField
        val ERROR_COORDINATE: Double = ConstantsUi.ERROR_COORDINATE
        @JvmField
        val CITY_IMAGE_LINK: String = "https://freepngimg.com/thumb/city/36275-3-city-hd.png"

        // Константы для локальных настроек программы

    }
}