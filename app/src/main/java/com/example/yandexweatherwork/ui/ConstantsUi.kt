package com.example.yandexweatherwork.ui

import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.domain.ConstantsDomain
import com.example.yandexweatherwork.repository.ConstantsRepository

class ConstantsUi {
    companion object {
        @JvmField
        val SHARED_SAVE: String = "sharedSave"
        @JvmField
        val SHARED_NUMBER_SAVED_CITIES: String = "numberSavedCities"
        @JvmField
        val SHARED_POSITION_CURRENT_KNOWN_CITY: String = "positionCurrentKnownCity"
        @JvmField
        val SHARED_DEFAULT_FILTER_CITY: String = "defaultFilterCity"
        @JvmField
        val SHARED_DEFAULT_FILTER_COUNTRY: String = "defaultFilterCountry"
        @JvmField
        val SHARED_USER_CORRECTED_CITY_LIST: String = "userCorrectedCityList"
        @JvmField
        val UNKNOWN_TEXT: String = "unknown"
        @JvmField
        val ZERO_FLOAT: Float = 0f
        @JvmField
        val FILTER_RUSSIA: String = ConstantsDomain.FILTER_RUSSIA
        @JvmField
        val FILTER_NOT_RUSSIA: String = ConstantsDomain.FILTER_NOT_RUSSIA
        @JvmField
        val DEFAULT_FILTER_CITY: String = ConstantsDomain.DEFAULT_FILTER_CITY
        @JvmField
        val ERROR_COORDINATE: Double = -9999999999999999.0
        @JvmField
        val BROADCAST_ACTION: String = ConstantsRepository.BROADCAST_ACTION
        @JvmField
        val INTERNET_SERVICE_STRING_KEY = ConstantsRepository.INTERNET_SERVICE_STRING_KEY
        @JvmField
        val WEATHER_DATA_INTENT_FILTER: String = ConstantsController.WEATHER_DATA_INTENT_FILTER
        @JvmField
        val LATITUDE_NAME : String = ConstantsRepository.LATITUDE_NAME
        @JvmField
        val LONGITUDE_NAME : String = ConstantsRepository.LONGITUDE_NAME
        @JvmField
        val MAIN_CHOOSER_GETTER: String = ConstantsController.MAIN_CHOOSER_GETTER
        @JvmField
        val MAIN_CHOOSER_SETTER: String = ConstantsController.MAIN_CHOOSER_SETTER
    }
}