package com.example.yandexweatherwork.repository.facadeuser

import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryGetCityCoordinates(cityName: String, mainChooserSetter: MainChooserSetter): Thread() {
    val cityName: String = cityName
    val mainChooserSetter: MainChooserSetter = mainChooserSetter

    override fun run() {
        // Открытие сессии
        var urlConnection: HttpURLConnection? = null
        val url =
            URL("https://nominatim.openstreetmap.org/search.php?q=$cityName&format=jsonv2")
        urlConnection = url.openConnection() as HttpsURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.readTimeout = 10000
        val inf = BufferedReader(InputStreamReader(urlConnection.inputStream))
        val answer = StringBuilder()

        // Распознавание ответа
        var line: String?
        while (inf.readLine().also { line = it } != null) {
            answer.append(line).append('\n')
        }

        // Анализ ответа
        val stringAnswer = answer.toString()
        stringAnswer?.let{
            if ((stringAnswer?.indexOf("\"lat\":") + 7 < stringAnswer.length)
                && (stringAnswer?.indexOf("\"lat\":") + 7 < stringAnswer?.indexOf("\"lon\":") - 2)
                && (stringAnswer?.indexOf("\"lon\":") + 8 < stringAnswer.length)) {
                val strLat = stringAnswer?.subSequence(
                    stringAnswer?.indexOf("\"lat\":") + 7,
                    stringAnswer?.indexOf("\"lon\":") - 2
                ) as String
                val startIndex: Int = stringAnswer?.indexOf("\"lon\":") + 8
                if ((startIndex < stringAnswer.length)
                    && (stringAnswer?.indexOf("\",\"", startIndex) < stringAnswer.length)) {
                        val strLon = stringAnswer?.subSequence(
                            stringAnswer?.indexOf("\"lon\":") + 7,
                            stringAnswer?.indexOf("\",\"", startIndex)
                        ) as String
                        mainChooserSetter.setLat(strLat.toDouble())
                        mainChooserSetter.setLon(strLon.toDouble())
                }
            }
        }
        // Закрытие сессии
        urlConnection.disconnect()
    }
}