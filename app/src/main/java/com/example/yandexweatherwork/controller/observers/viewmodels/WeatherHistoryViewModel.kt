package com.example.yandexweatherwork.controller.observers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexweatherwork.MyApp
import com.example.yandexweatherwork.repository.facadesettings.RepositorySettingsImpl

class WeatherHistoryViewModel (
    private val historyLiveDataToObserve: MutableLiveData<UpdateState> = MutableLiveData(),
    private val historyRepositoryImpl: RepositorySettingsImpl = RepositorySettingsImpl(MyApp.getHistoryDAO())
) :
    ViewModel() {
    fun getAllHistory() {
        historyLiveDataToObserve.value = UpdateState.Loading
//        historyLiveDataToObserve.postValue(UpdateState.SuccessWeatherHistory(historyRepositoryImpl.getAllHistory()))
        historyLiveDataToObserve.postValue(UpdateState.SuccessWeatherHistory(historyRepositoryImpl.getUniqueListCities()))
    }
    fun getLiveData() = historyLiveDataToObserve
}