package com.example.yandexweatherwork.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.yandexweatherwork.R
import com.example.yandexweatherwork.controller.ConstantsController
import com.example.yandexweatherwork.controller.navigations.content.NavigationContent
import com.example.yandexweatherwork.controller.navigations.content.NavigationGetterContent
import com.example.yandexweatherwork.controller.observers.domain.*
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModel
import com.example.yandexweatherwork.controller.observers.viewmodels.ListCitiesViewModelSetter
import com.example.yandexweatherwork.controller.observers.viewmodels.ResultCurrentViewModel
import com.example.yandexweatherwork.controller.observers.viewmodels.ResultCurrentViewModelSetter
import com.example.yandexweatherwork.domain.core.MainChooser
import com.example.yandexweatherwork.domain.data.City
import com.example.yandexweatherwork.domain.facade.MainChooserGetter
import com.example.yandexweatherwork.domain.facade.MainChooserSetter
import com.example.yandexweatherwork.ui.ConstantsUi


class MainActivity:
    AppCompatActivity(),
    ResultCurrentViewModelSetter,
    ListCitiesViewModelSetter,
    PublisherGetterDomain,
    ListSitiesPublisherGetterDomain,
    ObserverDomain,
    NavigationGetterContent {

    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var resultCurrentViewModel: ResultCurrentViewModel = ResultCurrentViewModel()
    private var listCitiesViewModel: ListCitiesViewModel = ListCitiesViewModel()
    private val publisherDomain: PublisherDomain = PublisherDomain()
    private val listCitiesPublisherDomain: ListCitiesPublisherDomain = ListCitiesPublisherDomain()
    private val mainChooser: MainChooser = MainChooser()
    private val mainChooserSetter: MainChooserSetter = MainChooserSetter(mainChooser)
    private val mainChooserGetter: MainChooserGetter = MainChooserGetter(mainChooser)
    private val navigationContent: NavigationContent = NavigationContent(supportFragmentManager,
        mainChooserSetter, mainChooserGetter)
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Подключение наблюдателей за domain к MainActivity
        publisherDomain.subscribe(this)
        listCitiesPublisherDomain.subscribe(this)

        // Случай первого запуска активити
        if (savedInstanceState == null) {
            // Получение известных городов
            getKnownCities()
            // Выбор
            if (mainChooserGetter.getPositionCurrentKnownCity() == -1)
                // Отображение фрагмента со списком мест (city) для выбора интересующего места
                navigationContent.addListCitiesFragment(mainChooserGetter
                    .getDefaultFilterCountry() == ConstantsController.FILTER_RUSSIA, false)
            else
                // Отображение фрагмента с прогнозом погоды по выбранному ранее месту
                navigationContent.addResultCurrentFragment(mainChooserGetter.getCurrentKnownCity()!!
                    , false)
        }

        // Установка AppBarMenu
        setupAppBarMenu()

/*
        val repositoryGetCitiInfo: RepositoryGetCitiInfo = RepositoryGetCitiInfo()
        repositoryGetCitiInfo.getCityInfo()
*/
/*
        val repositoryGetCityCoordinates: RepositoryGetCityCoordinates = RepositoryGetCityCoordinates("Москва", mainChooserSetter)
        repositoryGetCityCoordinates.start()
        Thread.sleep(2000)
        Toast.makeText(this, "${mainChooser.getLat()}; ${mainChooser.getLon()}", Toast.LENGTH_LONG).show()
*/
    }

    //region ФУНКЦИИ ДЛЯ APPBARMENU
    // Установка меню AppBarMenu
    private fun setupAppBarMenu() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }
    // Создание меню AppBarMenu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menu)
        return true
    }
    // Установка слушателя на меню AppBarMenu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_menu_action_add_city -> {
                // Добавить место
                Toast.makeText(this, "Добавить место", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    //endregion

    // Установка наблюдателя для обновления данных в ResultCurrentFragment
    override fun setResultCurrentViewModel(viewModel: ResultCurrentViewModel) {
        resultCurrentViewModel = viewModel
    }

    // Установка наблюдателя для обновления данных в ListCitiesFragment
    override fun setListCitiesViewModel(viewModel: ListCitiesViewModel) {
        listCitiesViewModel = viewModel
        listCitiesViewModel.setMainChooserGetter(mainChooserGetter)
    }

    override fun onStop() {
        super.onStop()
        // Сохранение известных городов
        saveKnownCities()
    }

    //region МЕТОДЫ ДЛЯ РАБОТЫ С SHAREDPREFERENCES
    // Сохранение настроек в SharedPreferences
    private fun saveKnownCities() {
        val numberKnownCities = mainChooserGetter.getNumberKnownCites()
        val sharedPreferences: SharedPreferences = getSharedPreferences(ConstantsUi.SHARED_SAVE, MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(ConstantsUi.SHARED_NUMBER_SAVED_CITIES, numberKnownCities)
        if (numberKnownCities > 0) {
            val nameStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "name$i"}
            val latStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lat$i"}
            val lonStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lone$i"}
            val countryStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "country$i"}
            val knownCities: List<City>? = mainChooserGetter.getKnownCites("","")
            knownCities?.let{
                var index: Int = 0
                it.forEach { element ->
                    editor.putString(nameStringArray[index], element.name)
                    editor.putFloat(latStringArray[index], element.lat.toFloat())
                    editor.putFloat(lonStringArray[index], element.lon.toFloat())
                    editor.putString(countryStringArray[index++], element.country)
                }
            }
        }
        with(editor) {
            putInt(ConstantsUi.SHARED_POSITION_CURRENT_KNOWN_CITY, mainChooserGetter
                .getPositionCurrentKnownCity())
            putString(ConstantsUi.SHARED_DEFAULT_FILTER_CITY, mainChooserGetter
                .getDefaultFilterCity())
            if (mainChooserGetter.getDefaultFilterCountry() == ConstantsUi.FILTER_RUSSIA) {
                putString(
                    ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY,
                    mainChooserGetter.getDefaultFilterCountry())
            } else {
                putString(
                    ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY,
                    ConstantsUi.FILTER_NOT_RUSSIA)
            }
            putBoolean(ConstantsUi.SHARED_USER_CORRECTED_CITY_LIST, mainChooserGetter
                .getUserCorrectedCityList())
            apply()
        }
    }

    // Получение настроек из SharedPreferences
    private fun getKnownCities() {
        val sharedPreferences: SharedPreferences = getSharedPreferences(ConstantsUi.SHARED_SAVE, MODE_PRIVATE)
        val numberKnownCities = sharedPreferences.getInt(ConstantsUi.SHARED_NUMBER_SAVED_CITIES, 0)
        if (numberKnownCities > 0) {
            val nameStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "name$i"}
            val latStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lat$i"}
            val lonStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "lone$i"}
            val countryStringArray: Array<String> = Array<String>(numberKnownCities) { i -> "country$i"}
            repeat(numberKnownCities) {
                mainChooserSetter.addKnownCities(City(
                    sharedPreferences.getString(nameStringArray[it], ConstantsUi.UNKNOWN_TEXT)!!,
                    sharedPreferences.getFloat(latStringArray[it], ConstantsUi.ZERO_FLOAT).toDouble(),
                    sharedPreferences.getFloat(lonStringArray[it], ConstantsUi.ZERO_FLOAT).toDouble(),
                    sharedPreferences.getString(countryStringArray[it], ConstantsUi.UNKNOWN_TEXT)!!))
            }
        }
        mainChooserSetter.also{
            it.setPositionCurrentKnownCity(sharedPreferences.getInt(ConstantsUi.SHARED_POSITION_CURRENT_KNOWN_CITY, -1))
            it.setDefaultFilterCity(sharedPreferences.getString(ConstantsUi.SHARED_DEFAULT_FILTER_CITY, "")!!)
            it.setDefaultFilterCountry(sharedPreferences.getString(ConstantsUi.SHARED_DEFAULT_FILTER_COUNTRY, "")!!)
            it.setUserCorrectedCityList(sharedPreferences.getBoolean(ConstantsUi.SHARED_USER_CORRECTED_CITY_LIST, false))
        }

        // Установка известных городов по-умолчанию
        if ((mainChooserGetter.getNumberKnownCites() == 0) && !mainChooserGetter.getUserCorrectedCityList()) {
            mainChooserSetter.initKnownCities()
            // Установка фильтра стран по-умолчанию
            mainChooserSetter.setDefaultFilterCountry(ConstantsUi.FILTER_RUSSIA)
        }
    }
    //endregion

    //region МЕТОДЫ ДЛЯ ПЕРЕДАЧИ РЕЗУЛЬТАТОВ ДЕЙСТВИЙ ПОЛЬЗОВАТЕЛЯ ВО ФРАГМЕНТАХ В КЛАСС MainChooser (domain)
    // Создание метода для передачи наблюдателя PublisherDomain для domain во фрагменты
    override fun getPublisherDomain(): PublisherDomain {
        return publisherDomain
    }
    // Создание метода для передачи наблюдателя ListCitiesPublisherDomain для domain во фрагменты
    override fun getListSitiesPublisherDomain(): ListCitiesPublisherDomain {
        return listCitiesPublisherDomain
    }
    override fun updateFilterCountry(filterCountry: String) {
        mainChooserSetter.setDefaultFilterCountry(filterCountry)
    }
    override fun updateFilterCity(filterCity: String) {
        mainChooserSetter.setDefaultFilterCity(filterCity)
    }
    override fun updatePositionCurrentKnownCity(positionCurrentKnownCity: Int) {
        mainChooserSetter.setPositionCurrentKnownCity(positionCurrentKnownCity)
    }

    // Установка наблюдателя для domain
    override fun updateCity(city: City) {
        mainChooserSetter.setPositionCurrentKnownCity(city.name, city.country)
        // Получение данных в resultCurrentViewModel
        resultCurrentViewModel.getDataFromRemoteSource(mainChooserSetter, mainChooserGetter)
    }
    //endregion

    override fun getNavigationContent(): NavigationContent {
        return navigationContent
    }
}