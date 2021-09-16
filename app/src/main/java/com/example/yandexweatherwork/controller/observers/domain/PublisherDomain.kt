package com.example.yandexweatherwork.controller.observers.domain

import com.example.yandexweatherwork.domain.data.City

class PublisherDomain {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var observers: MutableList<ObserverDomain>? = null
    //endregion

    // Подписаться на паблишер
    fun subscribe(observer: ObserverDomain) {
        if (observers == null) {
            observers = MutableList<ObserverDomain>(1) { observer }
        } else {
            observers?.add(observer)
        }
    }

    // Отписаться от паблишера
    fun unsubscribe(observer: ObserverDomain) = observers?.let{it.remove(observer)}

    // Разослать событие о действиях пользователя
    fun notifyCity(city: City) {
        observers?.let {
            for (observer in it) {
                observer.updateCity(city)
            }
        }
    }

    // Разослать событие о смене пользователем фильтра страны
    fun notifyDefaultFilterCity(defaultFilterCity: String) {
        observers?.let {
            for (observer in it) {
                observer.updateFilterCity(defaultFilterCity)
            }
        }
    }

    // Разослать событие о смене позиции текущего известного места (города)
    fun notifyPositionCurrentKnownCity(positionCurrentKnownCity: Int) {
        observers?.let {
            for (observer in it) {
                observer.updatePositionCurrentKnownCity(positionCurrentKnownCity)
            }
        }
    }
}