package com.example.yandexweatherwork.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.activities.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkChangeBroadcastReceiver: BroadcastReceiver() {
    var existConnection: Boolean = false
    var mainChooserSetter: MainChooserSetter? = null

    override fun onReceive(context: Context, intent: Intent?) {
        mainChooserSetter = (context as MainActivity).getMainChooserSetter()
        val BROADCAST_ACTION: String = ConstantsRepository.BROADCAST_ACTION
        val newAction = intent?.action
        if (intent != null) {
            when (newAction) {
                BROADCAST_ACTION -> {
                    val connMgr = context
                        .getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE)
                            as ConnectivityManager
                    val wifi = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    val mobile = connMgr
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    if (wifi!!.isAvailable || mobile!!.isAvailable) {
                        pingGoogleCite()
                        sleep(1000)
                        if (existConnection) {
                            Toast.makeText(context, "СПОСОБ №2: Связь и Интернет ЕСТЬ",
                                Toast.LENGTH_LONG).show()
                            Log.d("mylogs", "СПОСОБ №2: Связь и Интернет ЕСТЬ")
                            existConnection = false
                            // Установка признака наличия интернета в ядро приложения (MainChooser)
                            mainChooserSetter?.let{it.setExistInternet(true)}
                        } else {
                            Toast.makeText(context, "СПОСОБ №2: Связь есть, Интернета НЕТ",
                                Toast.LENGTH_LONG).show()
                            Log.d("mylogs", "СПОСОБ №2: Связь есть, Интернета НЕТ")
                            // Установка признака наличия интернета в ядро приложения (MainChooser)
                            mainChooserSetter?.let{it.setExistInternet(false)}
                        }
                    } else {
                        Toast.makeText(context, "СПОСОБ №2: Связи и Интернета НЕТ",
                            Toast.LENGTH_LONG).show()
                        Log.d("mylogs", "СПОСОБ №2: Связи и Интернета НЕТ")
                        // Установка признака наличия интернета в ядро приложения (MainChooser)
                        mainChooserSetter?.let{it.setExistInternet(false)}
                    }
                }
            }
        }
    }

    // Пингование сайта https://google.com как наиболее стабильно работающего сайта
    // Можно для повышения надёжности метода пропинговать несколько сайтов
    fun pingGoogleCite() {
        var stringAnswer = ""
        Thread {
            try {
                // Открытие сессии
                var urlConnection: HttpsURLConnection? = null
                val url = URL(ConstantsRepository.GOOGLE_URL_TO_PING)
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
                stringAnswer = answer.toString()

                // Закрытие сессии
                urlConnection.disconnect()

                // Сохранение результата
                existConnection = stringAnswer.isNotEmpty()
            } catch (e: Exception) {
                Log.d("pingGoogleCite: ", e.localizedMessage);
            }
        }.start()
    }
}