package com.example.yandexweatherwork.controller.navigations.content

import androidx.fragment.app.FragmentManager
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.controller.navigations.dialogs.NavigationDialogs
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.fragments.content.domain.ContactsRequestFragment
import com.example.yandexweatherwork.ui.fragments.content.domain.ListCitiesFragment
import com.example.yandexweatherwork.ui.fragments.content.domain.MapsFragment
import com.example.yandexweatherwork.ui.fragments.content.result.ResultCurrentFragment
import com.example.yandexweatherwork.ui.fragments.content.result.ResultWeatherHistoryFragment

class NavigationContent(
    private val fragmentManager: FragmentManager,
    private val mainChooserSetter: MainChooserSetter,
    private val mainChooserGetter: MainChooserGetter
) {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var mapsFragment: MapsFragment? = null
    private var navigationDialogs: NavigationDialogs? = null
    private var navigationCurSteps: ConstantsController.Companion.NavigationSteps? = null
    private var navigationPrevSteps: ConstantsController.Companion.NavigationSteps? = null
    //endregion

    //region Методы получения навигационной метки
    fun getNavigationCurSteps(): ConstantsController.Companion.NavigationSteps? {
        return navigationCurSteps
    }
    fun getNavigationPrevSteps(): ConstantsController.Companion.NavigationSteps? {
        return navigationPrevSteps
    }
    //endregion

    //region МЕТОДЫ ПОЛУЧЕНИЯ И УСТАНОВКИ КЛАССА NAVIGATIONDIALOGS
    fun getterNavigationDialogs(): NavigationDialogs? {
        return navigationDialogs
    }
    fun setterNavigationDialogs(navigationDialogs: NavigationDialogs) {
        this.navigationDialogs = navigationDialogs
    }
    //endregion

    //region МЕТОДЫ ПОЛУЧЕНИЯ И УСТАНОВКИ КЛАССА MAPSFRAGMENT
    fun gettergMapsFragment(): MapsFragment? {
        return mapsFragment
    }
    fun setterMapsFragment(mapsFragment: MapsFragment?) {
        this.mapsFragment = mapsFragment
    }
    //endregion

    // Установка геттеров для MainChooserSetter и MainChooserGetter
    fun getMainChooserSetter(): MainChooserSetter = mainChooserSetter
    fun getMainChooserGetter(): MainChooserGetter = mainChooserGetter

    // Отображение фрагмента с погодными результатами ResultCurrentFragment
    fun showResultCurrentFragment(city: City, doSaveResult: Boolean, useBackStack: Boolean) {
        if (doSaveResult) {
            mainChooserSetter.setIsDataWeatherFromLocalBase(false)
        } else {
            mainChooserSetter.setIsDataWeatherFromLocalBase(true)
        }
        // Открыть транзакцию
        fragmentManager?.let {
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                ResultCurrentFragment.newInstance(city, doSaveResult))
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
        //Установка навигационной метки
        navigationPrevSteps = navigationCurSteps
        navigationCurSteps = ConstantsController.Companion.NavigationSteps.RESULT_CURRENT_FRAGMENT
    }

    // Отображение фрагмента со списком мест ListCitiesFragment
    fun showListCitiesFragment(useBackStack: Boolean) {
        val isDataSetRusInitial = mainChooserGetter
            .getDefaultFilterCountry() == ConstantsController.FILTER_RUSSIA
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
        //Установка навигационной метки
        navigationPrevSteps = navigationCurSteps
        navigationCurSteps = ConstantsController.Companion.NavigationSteps.LIST_CITIES_FRAGMENT
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
        //Установка навигационной метки
        navigationPrevSteps = navigationCurSteps
        navigationCurSteps = ConstantsController.Companion.NavigationSteps
            .RESULT_WEATHER_HISTORY_FRAGMENT
    }

    // Отображение фрагмента с контактами
    fun showContactsFragment(useBackStack: Boolean) {
        // Открыть транзакцию
        fragmentManager?.let{
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                ContactsRequestFragment.newInstance())
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
        //Установка навигационной метки
        navigationPrevSteps = navigationCurSteps
        navigationCurSteps = ConstantsController.Companion.NavigationSteps.CONTACTS_FRAGMENT
    }

    // Отображение фрагмента с Google map
    fun showGoogleMapFragment(useBackStack: Boolean) {
        setterMapsFragment(mapsFragment)
        // Открыть транзакцию
        fragmentManager?.let{
            val fragmentTransaction = it.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_result_weather_container,
                MapsFragment.newInstance(mainChooserGetter, mainChooserSetter, this))
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null)
            }
            // Закрыть транзакцию
            fragmentTransaction.commit()
        }
        //Установка навигационной метки
        navigationPrevSteps = navigationCurSteps
        navigationCurSteps = ConstantsController.Companion.NavigationSteps.GOOGLE_MAP_FRAGMENT
    }
}