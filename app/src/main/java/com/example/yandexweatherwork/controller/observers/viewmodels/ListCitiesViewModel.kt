package com.example.yandexweatherwork.controller.observers.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter

class ListCitiesViewModel(
    private val liveDataToObserve: MutableLiveData<UpdateState> = MutableLiveData()
) : ViewModel() {
    private var mainChooserGetter: MainChooserGetter? = null

    fun setMainChooserGetter(mainChooserGetter: MainChooserGetter) {
        this.mainChooserGetter = mainChooserGetter
    }

    fun getLiveData() = liveDataToObserve

    fun getListCities() {
        if (mainChooserGetter != null) {
            // Передача данных в основном потоке postValue (postValue два раза подряд использовать нельзя)
            liveDataToObserve.postValue(UpdateState.ListCities(mainChooserGetter as MainChooserGetter))
        }
    }
}