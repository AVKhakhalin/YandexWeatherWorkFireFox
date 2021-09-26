package com.example.yandexweatherwork.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataForecastWeather (
    val city: City = getDefaultCity(),
    val date: String,           // date	Дата прогноза в формате ГГГГ-ММ-ДД.	Строка
    val dateTs: Long,           // date_ts	Дата прогноза в формате Unixtime.	Число
    val weekNumber: Int,        // week	Порядковый номер недели.	Число
    val sunriseTime: String,    // sunrise	Время восхода Солнца, локальное время (может отсутствовать для полярных регионов).	Строка
    val sunsetTime: String,     // sunset	Время заката Солнца, локальное время (может отсутствовать для полярных регионов).	Строка
    val moonPhaseCode: Int,     // moon_code	Код фазы Луны. Возможные значения:
    //  0 — полнолуние.
    //  1-3 — убывающая Луна.
    //  4 — последняя четверть.
    //  5-7 — убывающая Луна.
    //  8 — новолуние.
    //  9-11 — растущая Луна.
    //  12 — первая четверть.
    //  13-15 — растущая Луна.	Число
    val moonTextCode: String,   //moon_text	Текстовый код для фазы Луны. Возможные значения:
    //  moon-code-0 — полнолуние.
    //  moon-code-1 — убывающая луна.
    //  moon-code-2 — убывающая луна.
    //  moon-code-3 — убывающая луна.
    //  moon-code-4 — последняя четверть.
    //  moon-code-5 — убывающая луна.
    //  moon-code-6 — убывающая луна.
    //  moon-code-7 — убывающая луна.
    //  moon-code-8 — новолуние.
    //  moon-code-9 — растущая луна.
    //  moon-code-10 — растущая луна.
    //  moon-code-11 — растущая луна.
    //  moon-code-12 — первая четверть.
    //  moon-code-13 — растущая луна.
    //  moon-code-14 — растущая луна.
    //  moon-code-15 — растущая луна.	Строка
    val parts: ForecastsParts,  //	Прогнозы по времени суток. Содержит следующие поля:
    //  part_name
    //  temp_min
    //  temp_max
    //  temp_avg
    //  feels_like
    //  icon
    //  condition
    //  daytime
    //  polar
    //  wind_speed
    //  wind_gust
    //  wind_dir
    //  pressure_mm
    //  pressure_pa
    //  humidity
    //  prec_mm
    //  prec_period
    //  prec_prob
    //Все прогнозы погоды на время суток имеют одинаковый набор полей.
    //Ответ содержит прогноз на 2 ближайших периода.	Объект
) : Parcelable

private fun getDefaultCity() = City("Москва", 55.0, 37.0, "Россия")
