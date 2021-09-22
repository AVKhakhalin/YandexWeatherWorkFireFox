package com.example.yandexweatherwork.controller.navigations.content

import androidx.fragment.app.FragmentManager
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.content.result.ResultCurrentFragment

class NavigationContent(
    private val fragmentManager: FragmentManager,
    private val mainChooserSetter: MainChooserSetter,
    private val mainChooserGetter: MainChooserGetter
) {

    fun addResultCurrentFragment(city: City, useBackStack: Boolean) {
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

    fun addListCitiesFragment(isDataSetRusInitial: Boolean, useBackStack: Boolean) {
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
}