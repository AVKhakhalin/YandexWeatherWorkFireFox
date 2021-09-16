package com.example.yandexweatherwork.domain.data

class Fact (
    val temp: Float?,           // temp	Температура (°C).	Число
    val feels_like: Float?,     // feels_like	Ощущаемая температура (°C).	Число
    val temp_water: Float?,     // temp_water	Температура воды (°C). Параметр возвращается для населенных пунктов, где данная информация актуальна.	Число
    val icon: String?,          // icon	Код иконки погоды. Иконка доступна по адресу https://yastatic.net/weather/i/icons/funky/dark/<значение из поля icon>.svg.	Строка
    val condition: String?,     // condition	Код расшифровки погодного описания. Строка
    val wind_speed: Float?,     // wind_speed	Скорость ветра (в м/с).	Число
    val wind_gust: Float?,      // wind_gust	Скорость порывов ветра (в м/с).	Число
    val wind_dir: String?,      // wind_dir	Направление ветра. Строка
    val pressure_mm: Float?,     // pressure_mm	Давление (в мм рт. ст.).	Число
    val pressure_pa: Float?,    // pressure_pa	Давление (в гектопаскалях).	Число
    val humidity: Float?,       // humidity	Влажность воздуха (в процентах).	Число
    val daytime: String?,       // daytime	Светлое или темное время суток. Строка
    val polar: Boolean?,        // polar	Признак того, что время суток, указанное в поле daytime, является полярным.	Логический
    val season: String?        // season	Время года в данном населенном пункте. Строка
)