package com.example.yandexweatherwork.repository.facadeuser

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.example.yandexweatherwork.domain.data.DataModel
import com.example.yandexweatherwork.domain.data.DataWeather
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.repository.ConstantsRepository

class RepositoryWeatherImpl(private val mainChooserSetter: MainChooserSetter) : RepositoryWeather {
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
    private fun saveData(dataModel: DataModel?, lat: Double, lon: Double, error: Throwable?) = mainChooserSetter?.let{
        mainChooserSetter.setDataModel(dataModel, lat, lon, error)
    }
    //endregion
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