package com.example.yandexweatherwork.repository

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
        val YANDEX_KEY_VALUE : String = "ebbee072-d212-420e-9f62-4d716b0499e9"
        @JvmField
        val LATITUDE_NAME : String = "lat"
        @JvmField
        val LONGITUDE_NAME : String = "lon"
        @JvmField
        val LANGUAGE: String = "lang"

        // Константы для локальных настроек программы
    }
}