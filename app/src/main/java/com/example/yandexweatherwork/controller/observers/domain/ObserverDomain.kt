package com.example.yandexweatherwork.controller.observers.domain

import com.example.yandexweatherwork.domain.data.City

interface ObserverDomain {
    fun updateFilterCountry(filterCountry: String)
    fun updateFilterCity(filterCity: String)
    fun updateCity(city: City)
    fun updatePositionCurrentKnownCity(positionCurrentKnownCity: Int)
}