package com.example.yandexweatherwork.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataWeather(
    var city: City? = null,
    var temperature: Float? = null, //temp	Температура (°C).	Число
    var feelsLike: Float? = null,   //feels_like	Ощущаемая температура (°C).	Число
    var tempWater: Float? = null,   // temp_water	Температура воды (°C). Параметр возвращается для населенных пунктов, где данная информация актуальна.	Число
    var iconCode: String? = null,   // icon	Код иконки погоды. Иконка доступна по адресу https://yastatic.net/weather/i/icons/funky/dark/<значение из поля icon>.svg.	Строка
    var conditionCode: String? = null, // condition	Код расшифровки погодного описания. Возможные значения:
    //    clear — ясно.
    //    partly-cloudy — малооблачно.
    //    cloudy — облачно с прояснениями.
    //    overcast — пасмурно.
    //    drizzle — морось.
    //    light-rain — небольшой дождь.
    //    rain — дождь.
    //    moderate-rain — умеренно сильный дождь.
    //    heavy-rain — сильный дождь.
    //    continuous-heavy-rain — длительный сильный дождь.
    //    showers — ливень.
    //    wet-snow — дождь со снегом.
    //    light-snow — небольшой снег.
    //    snow — снег.
    //    snow-showers — снегопад.
    //    hail — град.
    //    thunderstorm — гроза.
    //    thunderstorm-with-rain — дождь с грозой.
    //    thunderstorm-with-hail — гроза с градом.	Строка
    var windSpeed: Float? = null,   // wind_speed	Скорость ветра (в м/с).	Число
    var windGust: Float? = null,    // wind_gust	Скорость порывов ветра (в м/с).	Число
    var windDirection: String? = null,  // wind_dir	Направление ветра. Возможные значения:
    //    «nw» — северо-западное.
    //    «n» — северное.
    //    «ne» — северо-восточное.
    //    «e» — восточное.
    //    «se» — юго-восточное.
    //    «s» — южное.
    //    «sw» — юго-западное.
    //    «w» — западное.
    //    «с» — штиль.	Строка
    var mmPresure: Float? = null,       // pressure_mm	Давление (в мм рт. ст.).	Число
    var paPressure: Float? = null,      // pressure_pa	Давление (в гектопаскалях).	Число
    var humidity: Float? = null,     // humidity	Влажность воздуха (в процентах).	Число
    var dayTime: String? = null,     // daytime	Светлое или темное время суток. Возможные значения:
    //    «d» — светлое время суток.
    //    «n» — темное время суток.	Строка
    var polar: Boolean? = null,      // polar	Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
    var season: String? = null,      // season	Время года в данном населенном пункте. Возможные значения:
    //    «summer» — лето.
    //    «autumn» — осень.
    //    «winter» — зима.
    //    «spring» — весна.	Строка
    var error: Throwable? = null //   Информация об ошибке загрузки погододных данных с сервера Yandex
): Parcelable