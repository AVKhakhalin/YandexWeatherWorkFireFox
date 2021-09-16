package com.example.yandexweatherwork.controller.observers.viewmodels

import com.example.yandexweatherwork.domain.facade.MainChooserGetter

sealed class UpdateState() {
    object Loading: UpdateState()
    data class Success(val mainChooserGetter: MainChooserGetter): UpdateState()
    data class Error(val error: Throwable?): UpdateState()
    data class ListCities(val mainChooserGetter: MainChooserGetter): UpdateState()
}