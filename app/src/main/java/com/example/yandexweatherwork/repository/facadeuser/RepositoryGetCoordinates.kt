package com.example.yandexweatherwork.repository.facadeuser

import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryGetCoordinates(cityName: String, mainChooserSetter: MainChooserSetter): Thread() {
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
        val string = answer.toString()
        val strLat = string?.subSequence(string?.indexOf("\"lat\":") + 7, string?.indexOf("\"lon\":") - 2) as String
        val startIndex: Int = string?.indexOf("\"lon\":") + 8
        val strLon = string?.subSequence(string?.indexOf("\"lon\":") + 7, string?.indexOf("\",\"", startIndex)) as String
        mainChooserSetter.setLat(strLat.toDouble())
        mainChooserSetter.setLon(strLon.toDouble())

        // Закрытие сессии
        urlConnection.disconnect()
    }
}