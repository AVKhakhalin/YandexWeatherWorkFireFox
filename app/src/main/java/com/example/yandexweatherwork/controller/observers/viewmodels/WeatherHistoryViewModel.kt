package com.example.yandexweatherwork.controller.observers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexweatherwork.MyApp
import com.example.yandexweatherwork.repository.facadesettings.RepositorySettingsImpl

class WeatherHistoryViewModel (
    private val historyLiveDataToObserve: MutableLiveData<UpdateState> = MutableLiveData(),
    private val historyRepositoryImpl: RepositorySettingsImpl =
        RepositorySettingsImpl(MyApp.getHistoryDAO())
):
    ViewModel() {
    fun getUniqueCitiesNames() {
        historyLiveDataToObserve.value = UpdateState.Loading
        Thread {
            historyLiveDataToObserve.postValue(UpdateState
                .SuccessGetUniqueCitiesWithWeatherHistory(historyRepositoryImpl.getUniqueListCities()))
        }.start()
    }

    fun getHistoryCityDataWeather(cityName: String) {
        historyLiveDataToObserve.value = UpdateState.Loading
        Thread {
            historyLiveDataToObserve.postValue(UpdateState
                .SuccessGetCityWeatherHistory(historyRepositoryImpl.getDataInHistory(cityName)))
        }.start()
    }
    fun getLiveData() = historyLiveDataToObserve
}