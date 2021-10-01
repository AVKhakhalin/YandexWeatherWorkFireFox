package com.example.yandexweatherwork.repository.facadeuser

import com.example.yandexweatherwork.domain.data.CityDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryGetCityInfo {
    fun getCityInfo(cityName: String, country: String): MutableList<CityDTO>? {
        var newCitiesInfoFiltred: MutableList<CityDTO>? = mutableListOf()
//        val url = URL("https://nominatim.openstreetmap.org/search.php?q=Анкара&format=json&limit=1")
//        val url = URL("https://nominatim.openstreetmap.org/search.php?q=Анкара&format=json")
        val url = URL("https://nominatim.openstreetmap.org/search.php?q=$cityName&format=json")
        Thread{
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod ="GET"
            urlConnection.readTimeout = 10000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val getCityDTOInfo: List<CityDTO> =
                Gson().fromJson(reader, object: TypeToken<ArrayList<CityDTO?>?>(){}.type)

//            val indexLastZapity: Int = getCityDTOInfo[0].display_name.lastIndexOf(", ") + 2
//            val country: String =
//                getCityDTOInfo[0].display_name.subSequence(indexLastZapity,
//                    getCityDTOInfo[0].display_name.length) as String
//            Log.d("mylogs", "$country ${getCityDTOInfo[0].lat}; " +
//                   "${getCityDTOInfo[0].lon}")
            urlConnection.disconnect()

            // Анализ полученной информации о месте по данным о стране
            if (country.isNotEmpty()) {
                getCityDTOInfo.forEach { cityDTO ->
                    if (cityDTO.display_name.indexOf(country) > 0) {
                        newCitiesInfoFiltred?.let { it.add(cityDTO) }
                    }
                }
                if ((getCityDTOInfo.isNotEmpty()) && (newCitiesInfoFiltred != null) && (newCitiesInfoFiltred.size == 0)) {
                    getAllDates(getCityDTOInfo, newCitiesInfoFiltred)
                }
            } else {
                getAllDates(getCityDTOInfo, newCitiesInfoFiltred)
            }
        }.start()
        sleep(1000)
        return newCitiesInfoFiltred
    }

    // Взять все полученные данные без фильтрации по названию страны
    private fun getAllDates(
        getCityDTOInfo: List<CityDTO>,
        citiesInfoFiltred: MutableList<CityDTO>?
    ) {
        getCityDTOInfo.forEach { cityDTO ->
            citiesInfoFiltred?.let { it.add(cityDTO) }
        }
    }
}