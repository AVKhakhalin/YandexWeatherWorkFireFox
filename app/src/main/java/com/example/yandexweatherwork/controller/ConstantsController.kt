package com.example.yandexweatherwork.controller

import com.example.yandexweatherwork.repository.ConstantsRepository
import com.example.yandexweatherwork.ui.ConstantsUi

class ConstantsController {
    companion object {
        @JvmField
        val ANSWER_LANGUAGE: String = "ru_RU"
        @JvmField
        val FILTER_RUSSIA: String = ConstantsUi.FILTER_RUSSIA
        @JvmField
        val LATITUDE_NAME : String = ConstantsRepository.LATITUDE_NAME
        @JvmField
        val LONGITUDE_NAME : String = ConstantsRepository.LONGITUDE_NAME
        @JvmField
        val LANGUAGE: String = ConstantsRepository.LANGUAGE
        @JvmField
        val DATA_INTENT_FILTER: String = "DATA_INTENT_FILTER"
        @JvmField
        val WEATHER_DATA_INTENT_FILTER: String = "WEATHER_DATA_INTENT_FILTER"
        @JvmField
        val MAIN_CHOOSER_GETTER: String = "mainChooserGetter"
        @JvmField
        val MAIN_CHOOSER_SETTER: String = "mainChooserSetter"
        @JvmField
        val DATES_LOADED: String = "DATES_LOADED"
        @JvmField
        val TIME_TO_WAIT_LOADING: Long = 500
        @JvmField
        val PUSH_KEY_TITLE: String = "title"
        @JvmField
        val PUSH_KEY_MESSAGE: String = "message"
        @JvmField
        val NOTIFICATION_ID_1: Int = 1
        @JvmField
        val CHANNEL_ID_1: String = "channel_id_1"
        @JvmField
        val ACTION_WORD: String = "action"
        @JvmField
        val ACTION_NAME: String = "actionName"
        @JvmField
        val CHANNEL_BASE_NAME: String = "Name"
        @JvmField
        val CHANNEL_BASE_DESCRIPTION: String = "Description"
    }
}