package com.example.yandexweatherwork.repository.facadeuser

import android.util.Log
import com.example.yandexweatherwork.domain.data.CityDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryGetCitiInfo {
    fun getCityInfo() { // TODO: Доработать входные параметры
//        val url = URL("https://nominatim.openstreetmap.org/search.php?q=Анкара&format=json&limit=1")
        val url = URL("https://nominatim.openstreetmap.org/search.php?q=Анкара&format=json")
        Thread{
            val urlConnection = url.openConnection() as HttpsURLConnection
            urlConnection.requestMethod ="GET"
            urlConnection.readTimeout = 10000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val getCityDTOInfo: List<CityDTO> =
                Gson().fromJson(reader, object: TypeToken<ArrayList<CityDTO?>?>(){}.type)
            // TODO: Уточнить входной индекс [0]
            val indexLastZapity: Int = getCityDTOInfo[0].display_name.lastIndexOf(", ") + 2
            val country: String =
                getCityDTOInfo[0].display_name.subSequence(indexLastZapity,
                    getCityDTOInfo[0].display_name.length) as String
//            Log.d("mylogs", "$country ${getCityDTOInfo[0].lat}; " +
//                    "${getCityDTOInfo[0].lon}")
            urlConnection.disconnect()
        }.start()
    }
}