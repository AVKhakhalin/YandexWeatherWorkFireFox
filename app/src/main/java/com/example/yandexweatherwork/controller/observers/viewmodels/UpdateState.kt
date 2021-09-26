package com.example.yandexweatherwork.controller.observers.viewmodels

import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.facade.MainChooserGetter

sealed class UpdateState() {
    object Loading: UpdateState()
    data class Success(val dataWeather: DataWeather, val city: City): UpdateState()
    data class Error(val error: Throwable?): UpdateState()
    data class ListCities(val mainChooserGetter: MainChooserGetter): UpdateState()
}