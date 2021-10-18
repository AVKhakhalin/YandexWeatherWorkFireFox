package com.example.yandexweatherwork.repository

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.activities.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkChangeBroadcastReceiver: BroadcastReceiver() {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    // Переменные для обработки сообщений внутри приложения
    var existConnection: Boolean = false
    var mainChooserSetter: MainChooserSetter? = null

    // Переменные для обработки внешних сообщений (приёма внешнего сообщения о новом месте)
    private var messageId = 0
    private var city: City? = null
    //endregion

    override fun onReceive(context: Context, intent: Intent?) {
        val BROADCAST_ACTION: String = ConstantsRepository.BROADCAST_ACTION
        val BROADCAST_ACTION_NEW_CITY = ConstantsRepository.BROADCAST_ACTION_NEW_CITY

        var newAction = intent?.action
        if (intent != null) {
            when (newAction) {
                BROADCAST_ACTION -> {
                    mainChooserSetter = (context as MainActivity).getMainChooserSetter()
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
//                            Toast.makeText(context, "СПОСОБ №2: Связь и Интернет ЕСТЬ",
//                                Toast.LENGTH_LONG).show()
                            Log.d("mylogs", "СПОСОБ №2: Связь и Интернет ЕСТЬ")
                            existConnection = false
                            // Установка признака наличия интернета в ядро приложения (MainChooser)
                            mainChooserSetter?.let{it.setExistInternet(true)}
                        } else {
//                            Toast.makeText(context, "СПОСОБ №2: Связь есть, Интернета НЕТ",
//                                Toast.LENGTH_LONG).show()
                            Log.d("mylogs", "СПОСОБ №2: Связь есть, Интернета НЕТ")
                            // Установка признака наличия интернета в ядро приложения (MainChooser)
                            mainChooserSetter?.let{it.setExistInternet(false)}
                        }
                    } else {
//                        Toast.makeText(context, "СПОСОБ №2: Связи и Интернета НЕТ",
//                            Toast.LENGTH_LONG).show()
                        Log.d("mylogs", "СПОСОБ №2: Связи и Интернета НЕТ")
                        // Установка признака наличия интернета в ядро приложения (MainChooser)
                        mainChooserSetter?.let{it.setExistInternet(false)}
                    }
                }

                BROADCAST_ACTION_NEW_CITY -> {
                    // Получение информации о новом месте из другого приложения
                    val cityName: String? = intent.getStringExtra(ConstantsRepository.NAME_MSG_CITY_NAME)
                    val cityLat: Double? = intent.getDoubleExtra(ConstantsRepository.NAME_MSG_CITY_LAT, ConstantsRepository.ERROR_COORDINATE)
                    val cityLon: Double? = intent.getDoubleExtra(ConstantsRepository.NAME_MSG_CITY_LON, ConstantsRepository.ERROR_COORDINATE)
                    val cityCountry: String? = intent.getStringExtra(ConstantsRepository.NAME_MSG_CITY_COUNTRY)

                    // Передача новой информации в класс city
                    if ((cityName != null) && (cityLat != null) && (cityLon != null) && (cityCountry != null)) {
                        city = City(cityName, cityLat, cityLon, cityCountry)
                        mainChooserSetter?.let { it.addKnownCities(city!!)}
                    }

                    // Вывод информации в лог
                    Log.d("mylogs", "Получена информация о новом месте: $cityName; $cityLat; $cityLon; $cityCountry")

                    // Создать в приложении нотификацию
                    val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "2")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Получена информация о новом месте:")
                        .setContentText(cityName)
                    val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.notify(messageId++, builder.build())
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