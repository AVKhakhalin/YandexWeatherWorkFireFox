package com.example.yandexweatherwork.controller.observers.viewmodels

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataModel
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.ConstantsRepository
import com.example.yandexweatherwork.repository.facadeuser.GetDataFromInternetService
import com.example.yandexweatherwork.repository.facadeuser.RepositoryWeatherImpl
import kotlin.coroutines.coroutineContext

class ResultCurrentViewModel(
    private val liveDataToObserve: MutableLiveData<UpdateState> = MutableLiveData()
): ViewModel() {
    private var repositoryWeatherImpl: RepositoryWeatherImpl? = null

    private var activityContext: Context? = null

    fun setActivityContext(activityContext: Context) {
        this.activityContext = activityContext
    }

    fun getLiveData() = liveDataToObserve

    fun getDataFromRemoteSource(dataWeather: DataWeather?, city: City?) {
        // Отслеживание состояния загрузки погодных данных
        with(liveDataToObserve) {
            // Отправка сообщения О ПРОЦЕССЕ ЗАГРУЗКИ
            postValue(UpdateState.Loading)
            Thread {
                Thread.sleep(ConstantsController.TIME_TO_WAIT_LOADING)
                if ((dataWeather != null) && (city != null) && (dataWeather.error == null)
                    && (dataWeather.temperature != null)) {
                    // УСПЕШНАЯ ПЕРЕДАЧА погодных данных в основном потоке через postValue (postValue два раза подряд использовать нельзя)
                    postValue(UpdateState.Success(dataWeather, city))
                } else {
                    // Передача СООБЩЕНИЯ ОБ ОШИБКЕ при получении погодных данных с сервера Yandex
                    if (dataWeather != null) {
                        postValue(UpdateState.Error(dataWeather?.error))
                    } else {
                        postValue(UpdateState.Error(Throwable(activityContext?.resources?.getString(R.string.error_dataweather_null))))
                    }
                }
            }.start()
        }
    }
}