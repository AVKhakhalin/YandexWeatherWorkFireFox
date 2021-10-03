package com.example.yandexweatherwork.controller.navigations.content

import androidx.fragment.app.FragmentManager
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.content.result.ResultCurrentFragment
import com.example.yandexweatherwork.ui.fragments.content.result.ResultWeatherHistoryFragment

class NavigationContent(
    private val fragmentManager: FragmentManager,
    private val mainChooserSetter: MainChooserSetter,
    private val mainChooserGetter: MainChooserGetter
) {
    // Установка геттеров для MainChooserSetter и MainChooserGetter
    fun getMainChooserSetter(): MainChooserSetter = mainChooserSetter
    fun getMainChooserGetter(): MainChooserGetter = mainChooserGetter

    // Отображение фрагмента с погодными результатами ResultCurrentFragment
    fun showResultCurrentFragment(city: City, useBackStack: Boolean) {
        // Открыть транзакцию
        fragmentManager?.let {
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                ResultCurrentFragment.newInstance(city))
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
    }

    // Отображение фрагмента со списком мест ListCitiesFragment
    fun showListCitiesFragment(isDataSetRusInitial: Boolean, useBackStack: Boolean) {
        // Открыть транзакцию
        fragmentManager?.let {
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                ListCitiesFragment.newInstance(isDataSetRusInitial,
                    mainChooserSetter, mainChooserGetter))
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
    }

    // Отображение фрагмента с историей погодных данных ResultWeatherHistoryFragment
    fun showResultWeatherHistoryFragment(useBackStack: Boolean) {
        // Открыть транзакцию
        fragmentManager?.let{
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                ResultWeatherHistoryFragment.newInstance())
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
    }
}