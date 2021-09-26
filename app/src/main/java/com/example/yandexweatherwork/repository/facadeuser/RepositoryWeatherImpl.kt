package com.example.yandexweatherwork.repository.facadeuser

import android.content.Context
import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.data.DataModel
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.data.Fact
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.ConstantsRepository
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

class RepositoryWeatherImpl(
    private val mainChooserSetter: MainChooserSetter,
    private val contextToBroadcastReceiver: Context?
    ) : RepositoryWeather {
    private val retrofitImpl: RetrofitImpl = RetrofitImpl()

    // Получение данных с сервера Yandex
    override fun getWeatherFromRemoteSource(lat: Double, lon: Double, lang: String) = sendServerRequest(lat, lon, lang)

    // Получение данных из локального источника
    override fun getWeatherFromLocalSource(): DataWeather = DataWeather()

    //region МЕТОДЫ ПОЛУЧЕНИЯ ДАННЫХ С СЕРВЕРА YANDEX
    private fun sendServerRequest(lat: Double, lon: Double, lang: String) {
        retrofitImpl.getWeatherApi()
            .getWeather(ConstantsRepository.YANDEX_KEY_VALUE, lat, lon, lang)
            .enqueue(object : Callback<DataModel> {
                override fun onResponse(
                    call: Call<DataModel>,
                    response: Response<DataModel>
                ) {
                    if ((response.isSuccessful) && (response.body() != null)) {
                        saveData(response.body(), lat, lon, null)
                    } else {
                        saveData(null, lat, lon, Throwable("Ответ от сервера пустой"))
                    }
                }

                override fun onFailure(call: Call<DataModel>, error: Throwable) {
                    saveData(null, lat, lon, error)
                }
            })
    }

    // Сохранение данных из dataModel в MainChooser (core)
    private fun saveData(dataModel: DataModel?, lat: Double, lon: Double, error: Throwable?)
    = mainChooserSetter?.let{
        mainChooserSetter.setDataModel(dataModel, lat, lon, error)

        // Запуск BroadcastReceiver для отображения обновлённых данных
        val mySendIntent = Intent(ConstantsRepository.WEATHER_DATA_INTENT_FILTER)
        val dataWeather: DataWeather? = createDataWeather(dataModel?.fact, lat, lon, error)
        mySendIntent.putExtra(ConstantsRepository.WEATHER_DATA,
            dataWeather)
        contextToBroadcastReceiver?.let{
            LocalBroadcastManager.getInstance(contextToBroadcastReceiver!!)
                .sendBroadcast(mySendIntent)
        }
    }
    //endregion


    private fun createDataWeather(fact: Fact?, lat: Double, lon: Double, error: Throwable?)
    : DataWeather? {
        var dataWeather: DataWeather? = null
        if (fact != null) {
            dataWeather = DataWeather()
                .apply{
                    this.city = City("", lat, lon, "")
                    this.temperature = fact.temp
                    this.feelsLike = fact.feels_like
                    this.tempWater = fact.temp_water
                    this.iconCode = fact.icon
                    this.conditionCode = fact.condition
                    this.windSpeed = fact.wind_speed
                    this.windGust = fact.wind_gust
                    this.windDirection = fact.wind_dir
                    this.mmPresure = fact.pressure_mm
                    this.paPressure = fact.pressure_pa
                    this.humidity = fact.humidity
                    this.dayTime = fact.daytime
                    this.polar = fact.polar
                    this.season = fact.season
                    this.error = error
                }
        } else {
            dataWeather = DataWeather()
                .apply {
                    this.city = null
                    this.temperature = null
                    this.feelsLike = null
                    this.tempWater = null
                    this.iconCode = null
                    this.conditionCode = null
                    this.windSpeed = null
                    this.windGust = null
                    this.windDirection = null
                    this.mmPresure = null
                    this.paPressure = null
                    this.humidity = null
                    this.dayTime = null
                    this.polar = null
                    this.season = null
                    this.error = error
                }
        }
        return dataWeather
    }
}

interface WeatherAPI {
    @GET("v2/informers")
    fun getWeather(
        @Header("X-Yandex-API-Key") token: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") lang: String
    ): Call<DataModel>
}

class RetrofitImpl {
    fun getWeatherApi(): WeatherAPI {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(ConstantsRepository.BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
            .build()
        return retrofit.create(WeatherAPI::class.java)
    }
}