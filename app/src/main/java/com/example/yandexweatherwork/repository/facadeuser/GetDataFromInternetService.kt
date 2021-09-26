package com.example.yandexweatherwork.repository.facadeuser

import android.app.IntentService
import android.content.Intent
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.ConstantsRepository

class GetDataFromInternetService(key: String = ConstantsRepository.WEATHER_DATA_INTENT_FILTER)
    : IntentService(key) {

    var mainChooserGetter: MainChooserGetter? = null
    var mainChooserSetter: MainChooserSetter? = null
    var lat: Double = 0.0
    var lon: Double = 0.0

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            lat = it.getDoubleExtra(ConstantsRepository.LATITUDE_NAME, 0.0)!!
            lon = it.getDoubleExtra(ConstantsRepository.LONGITUDE_NAME, 0.0)!!
            mainChooserGetter = it.getParcelableExtra(ConstantsRepository.MAIN_CHOOSER_GETTER)
            mainChooserSetter = it.getParcelableExtra(ConstantsRepository.MAIN_CHOOSER_SETTER)

            mainChooserSetter?.let {mainChooserSetter ->
            var repositoryWeatherImpl: RepositoryWeatherImpl? =
                RepositoryWeatherImpl(mainChooserSetter, applicationContext)
                repositoryWeatherImpl?.let {repositoryWeatherImpl ->
                    repositoryWeatherImpl.getWeatherFromRemoteSource(
                        mainChooserGetter?.getCurrentKnownCity()!!.lat,
                        mainChooserGetter?.getCurrentKnownCity()!!.lon, ConstantsRepository.LANGUAGE
                    )
                }
            }
        }
    }

/*    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }

    private fun createLogMessage(message: String) {
        //createLogMessage("createLogMessage")
        Log.d("mylogs", message)
    }*/
}